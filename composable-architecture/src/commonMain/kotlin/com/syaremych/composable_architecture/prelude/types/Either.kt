@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.prelude.types

import kotlin.coroutines.cancellation.CancellationException

typealias Option<A> = Either<Unit, A>

inline fun <A> A?.toOption() = this?.right() ?: Unit.left()
inline val <A> Option<A>.isEmpty: Boolean
    get() = this is Either.Left
inline val <A> Option<A>.value: A
    get() = (this as Either.Right).value
inline val <A> Option<A>.orNull: A?
    get() = (this as? Either.Right)?.value
inline fun <A> empty(): Either<Unit, A> = Unit.left()

sealed class Either<out L, out R> {

    data class Left<L>(val value: L) : Either<L, Nothing>()
    data class Right<R>(val value: R) : Either<Nothing, R>()

    fun <C> fold(ifLeft: (L) -> C, ifRight: (R) -> C): C =
        when (this) {
            is Left -> ifLeft(value)
            is Right -> ifRight(value)
        }

    companion object
}

fun <T> T.left() = Either.Left(this)
fun <T> T.right() = Either.Right(this)

fun <L, R> Either<L, R>.swap(): Either<R, L> = fold({ Either.Right(it) }, { Either.Left(it) })

fun <L, R1, R2> Either<L, R1>.flatMap(f: (R1) -> Either<L, R2>): Either<L, R2> =
    fold({ Either.Left(it) }, f)

fun <L, R1, R2> Either<L, R1>.rmap(f: (R1) -> R2): Either<L, R2> =
    fold({ Either.Left(it) }, { Either.Right(f(it)) })

fun <L1, L2, R> Either<L1, R>.lmap(f: (L1) -> L2): Either<L2, R> =
    swap().rmap(f).swap()

fun <T> Either.Companion.ofNullable(value: T?): Either<Unit, T> =
    value?.let { v -> Either.Right(v as T) } ?: Either.Left(Unit)

@OptIn(ExperimentalStdlibApi::class)
inline fun <R> Either.Companion.catch(f: () -> R): Either<Throwable, R> =
    try {
        Either.Right(f())
    } catch (t: Throwable) {
        if (t is CancellationException) {
            throw t
        }
        Either.Left(t)
    }

@Suppress("UNUSED_PARAMETER")
inline fun <T> emptyList(t: Throwable) = emptyList<T>()