package com.ezoky.ezmodel.core


object EzModel {

  import Atoms.Model
  import Domains._
  import UseCases._
  import Structures._

  import com.ezoky.ezmodel.storage.Repository

  var domainRepository = Repository[Domain](Model)
  var useCaseRepository = Repository[UseCase](Model)
  var entityRepository = Repository[Structure[Entity]](Model)
  var stateMachineRepository = Repository[StateMachine](Model)

  def reset() {
    domainRepository = Repository[Domain](Model)
    useCaseRepository = Repository[UseCase](Model)
    entityRepository = Repository[Structure[Entity]](Model)
    stateMachineRepository = Repository[StateMachine](Model)
  }
}