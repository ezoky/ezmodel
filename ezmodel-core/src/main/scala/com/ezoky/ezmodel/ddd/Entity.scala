package com.ezoky.ezmodel.ddd

import com.ezoky.ezmodel.ddd.Identity._

object Entity {
  def defaultIdentity[S, I]: Identity[S, I] = (state => state.stateValue.asInstanceOf[I])
}

/**
 * @author gweinbach
 */
sealed case class Entity[S, I](state: ValuedState[S], identity: Identity[S, I] = Entity.defaultIdentity[S, I]) {

  val id = identity(state)

  val isInitial = state match {
    case InitialState(_) => true
    case _ => false
  }

  val isFinal = state match {
    case FinalState(_) => true
    case _ => false
  }

  def +(nextState: State[S]): Entity[S, I] = changeState(nextState)

  @throws(classOf[EntityIdentityMustNotMutate])
  @throws(classOf[TargetStateHasNoValue])
  def changeState(nextState: State[S]): Entity[S, I] = {
    val targetState:State[S] = state + nextState
    targetState match {
      case ValuedState(_) =>
        val valuedState = targetState.asInstanceOf[ValuedState[S]]
        if (identity(valuedState) != identity(state)) {
          throw new EntityIdentityMustNotMutate()
        }
        copy(state = valuedState)

      case _ => throw new TargetStateHasNoValue()
    }
  }

  def hasSameIdentity(other: Entity[_, _]) = id.equals(other.id)

  def hasSameState(other: Entity[_, _]) = state.equals(other.state)

  def isIdentical(other: Entity[_, _]) = hasSameIdentity(other) && hasSameState(other)

  override def equals(other: Any) = other match {
    case otherEntity: Entity[_, _] => hasSameIdentity(otherEntity)
    case _ => false
  }

  override def hashCode() = id.hashCode()
}

class EntityIdentityMustNotMutate() extends RuntimeException

class TargetStateHasNoValue() extends RuntimeException
