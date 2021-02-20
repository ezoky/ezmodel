

  import com.ezoky.ezmodel.core.Atoms._
  import com.ezoky.ezmodel.core.Domains._
  import com.ezoky.ezmodel.core.Entities._
  import com.ezoky.ezmodel.core.EzModel._
  import com.ezoky.ezmodel.core.UseCases._
  import com.ezoky.ezmodel.core._
  import com.ezoky.ezmodel.core.EzModel

  val ezModel = EzModel.reset()
  import ezModel._

  val dom = Domain("Driving").useCase("Driver", "change", "Gear").useCase("Driver", "brake").entity("Gear")

  dom.useCases
  dom.entities
  println(dom)

  val monthlyInvoicing = asAn("Accountant") iWantTo("invoice", (a, "Month")) preCondition EntityState("Production", "Done") postCondition EntityState("Current Month", "Invoiced")
  monthlyInvoicing.constraints

  Domain("Accounting") useCase (monthlyInvoicing)

  val offre = Entity("Offre") aggregate(multiple, Entity("Gamme") aggregate(multiple, Entity("Sous-Gamme") aggregate(multiple, "Prestation"))) attribute ("nom")
  offre.name
  offre.attributes
  offre.aggregates
  offre.references

  domainRepository.populate
  domainRepository.queryEntitiesCount
  domainRepository.queryEntities.foreach(println)
  domainRepository.queryAllVersionsCount

  entityRepository.populate
  entityRepository.queryEntitiesCount
  entityRepository.queryEntities.foreach(println)
  entityRepository.queryAllVersionsCount

  useCaseRepository.populate
  useCaseRepository.queryEntitiesCount
  useCaseRepository.queryEntities.foreach(println)
  useCaseRepository.queryAllVersionsCount