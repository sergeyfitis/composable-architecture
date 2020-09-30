@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


typealias Reduced<Value, Action> = Pair<Value, Effect<Action>>

inline fun <Value, Action> reduced(
    value: Value,
    effect: Effect<Action>
): Reduced<Value, Action> = value to effect

class Store<Value : Any, Action : Any> private constructor(
    storeDispatcher: CoroutineDispatcher
) {

    private lateinit var _valueHolder: MutableStateFlow<Value>

    val valueHolder: StateFlow<Value>
        get() = _valueHolder

    private lateinit var reducer: Reducer<Value, Action, Any>

    private lateinit var environment: Any

    internal val storeScope = CoroutineScope(SupervisorJob() + storeDispatcher)

    internal var onStoreReleased: (() -> Unit)? = null

    internal fun send(action: Action) {
        storeScope.launch {
            val (value, effect) = reducer(_valueHolder.value, action, environment)
            this@Store._valueHolder.value = value
//            ensureActive()
            if (!effect.isEmpty) {
                effect
                    .onEach {
                        send(it)
                    }
                    .launchIn(this)
            }
        }
    }

    fun <LocalValue : Any, LocalAction : Any> scope(
        toLocalValue: (Value) -> LocalValue,
        toGlobalAction: (LocalAction) -> Action
    ): Store<LocalValue, LocalAction> {
        val localStore = init<LocalValue, LocalAction, Any>(
            initialState = toLocalValue(_valueHolder.value),
            reducer = Reducer { _, localAction, _ ->
                this.send(toGlobalAction(localAction))
                val localValue = toLocalValue(_valueHolder.value)
                return@Reducer reduced(localValue, Effect.none())
            },
            environment = environment
        )

        _valueHolder
            .map { newValue -> toLocalValue(newValue) }
            .onEach { localStore._valueHolder.value = it }
            .launchIn(storeScope)

        return localStore
    }

    fun release() {
        onStoreReleased?.invoke()
        onStoreReleased = null
        storeScope.cancel()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <Value : Any, Action : Any, Environment : Any> init(
            initialState: Value,
            reducer: Reducer<Value, Action, Environment>,
            environment: Environment,
            storeDispatcher: CoroutineDispatcher = Dispatchers.Default
        ): Store<Value, Action> {
            val store = Store<Value, Action>(storeDispatcher)
            store._valueHolder = MutableStateFlow(initialState)
            store.environment = environment
            store.reducer = Reducer { value, action, env ->
                val (state, effect) = reducer(value, action, env as Environment)
                return@Reducer reduced(state, effect)
            }
            return store
        }
    }
}
