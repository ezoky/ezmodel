package com.ezoky.ezmodel.actor

import akka.actor.{ActorRef, Props}
import akka.event.LoggingReceive
import com.ezoky.ezmodel.actor.Clerk.{Command, Event}
import com.ezoky.ezmodel.core.Atoms.Name
import com.ezoky.ezmodel.core.Domains.Domain
import com.ezoky.ezmodel.core.Entities.Entity
import com.ezoky.ezmodel.core.UseCases.UseCase

/**
 * @author gweinbach
 */
object DomainClerk {

  import com.ezoky.ezmodel.core.UseCases.{Actor,Goal}

  type DomainCommand = Command[Name]
  type DomainEvent = Event[Domain]

  case class CreateDomain(name: Name) extends DomainCommand(name)
  case class DomainCreated(domain:Domain) extends DomainEvent(domain)

  case class CreateEntity(domainName:Name,name: Name) extends DomainCommand(domainName)
  case class EntityAdded(domain: Domain) extends DomainEvent(domain)

  case class CreateUseCase(domainName:Name,actor: Actor, goal: Goal) extends DomainCommand(domainName)
  case class UseCaseAdded(domain: Domain) extends DomainEvent(domain)

}

trait DomainFactory extends Factory[Domain, Name] {

  import com.ezoky.ezmodel.actor.DomainClerk._

  override def createCommand = CreateDomain(_)
  override def createAction = Domain(_)
  override def createdEvent = DomainCreated(_)
}

class DomainClerk(name:Name) extends Clerk[Domain,Name] with DomainFactory {

  import com.ezoky.ezmodel.actor.EntityClerk._
  import com.ezoky.ezmodel.actor.DomainClerk._

  override def businessId = name

  val useCases = context.actorOf(Props(Office[UseCaseClerk]),"UseCases")
  val entities = context.actorOf(Props(Office[EntityClerk]),"Entities")

  override def receiveCommand = LoggingReceive {

    case DomainClerk.CreateEntity(_, entityName) =>
      entities ! EntityClerk.CreateEntity(entityName)

    case EntityClerk.EntityCreated(entity) =>
      val next = state.entity(entity)
      persist(EntityAdded(next))(updateState)

    case DomainClerk.CreateUseCase(_,actor, goal) =>
      useCases ! UseCaseClerk.CreateUseCase(actor,goal)

    case UseCaseClerk.UseCaseCreated(useCase) =>
      val next = state.useCase(useCase)
      persist(UseCaseAdded(next))(updateState)
  }
}