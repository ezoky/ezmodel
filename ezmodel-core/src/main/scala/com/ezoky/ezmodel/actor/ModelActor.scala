package com.ezoky.ezmodel.actor

import akka.actor.Actor.Receive
import akka.actor.{Props, Actor}

/**
 * @author gweinbach
 */
class ModelActor extends Actor {

  val domains = context.actorOf(Props(Office[DomainActor]),"Domains")
  //val stateMachines = context.actorOf(Props(Office[StateMachineActor]),"StateMachines")

  override def receive: Receive = ???
}