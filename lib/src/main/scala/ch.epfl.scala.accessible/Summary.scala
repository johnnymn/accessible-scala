package ch.epfl.scala.accessible

import scala.meta._

object Summary {

  def apply(tree: Tree): String =
    visitNames(tree, None)

  def apply(tree: Tree, offset: Offset): String =
    visitNames(tree, Some(offset))

  def apply(tree: Tree, offset: Option[Offset]): String =
    visitNames(tree, offset)

  def apply(tree: Tree, range: Range): String =
    visitNamesRange(tree, range)

  private val nl = "\n"

  private def spaces(in: String): String = in.replaceAllLiterally(".", " ")

  private def visitNamesStats(stats: List[Tree],
                              fun: Tree => String,
                              end: Boolean = false): String = {
    val ends = if (end) "." else ""
    stats.map(fun).filter(_.nonEmpty).mkString("", "," + nl, ends)
  }

  private def visitNames(tree: Tree, offset: Option[Offset]): String = {
    offset match {
      case Some(pos) =>
        find(tree, pos) match {
          case Some(subtree) => childrens(subtree)
          case None          => "cannot find tree"
        }
      case None => visitNames(tree)
    }
  }

  private def visitNamesRange(tree: Tree, range: Range): String = {
    find(tree, range) match {
      case Some(subtree) => childrens(subtree)
      case None          => "cannot find tree"
    }
  }

  private def childrens(tree: Tree): String = tree match {
    case t: Defn.Object => childrens("object", t.name.value, t.templ.stats)
    case t: Defn.Class  => childrens("class", t.name.value, t.templ.stats)
    case t: Pkg         => childrens("package", packageName(t.ref), t.stats)
    case t: Pkg.Object  => childrens("package object", t.name.value, t.templ.stats)
    case t: Template    => childrens(t.stats)
    case e              => visitDefinition(e)
  }

  private def childrens(stats: List[Stat]): String = {
    val statsRes = visitNamesStats(stats, visitDefinition, end = true)
    val sep =
      if (statsRes.contains(nl)) nl
      else " "
    sep + statsRes
  }

  private def childrens(node: String, name: String, stats: List[Stat]): String = {
    s"$node $name:" + childrens(stats)
  }

  def visitDefinition(subtree: Tree): String = subtree match {
    case Defn.Val(_, List(Pat.Var(name)), _, _) => s"val $name"
    case Defn.Var(_, List(Pat.Var(name)), _, _) => s"val $name"
    case t: Defn.Def                            => s"def ${t.name}"
    case t: Defn.Macro                          => s"macro ${t.name}"
    case t: Defn.Type                           => s"type ${t.name}"
    case t: Defn.Class                          => s"class ${t.name}"
    case t: Defn.Trait                          => s"trait ${t.name}"
    case t: Defn.Object                         => s"object ${t.name}"
    case t: Pkg.Object                          => s"package object ${t.name}"
    case t: Pkg                                 => s"package ${t.ref}"
    case Decl.Val(_, List(Pat.Var(name)), _)    => s"val $name"
    case Decl.Var(_, List(Pat.Var(name)), _)    => s"var $name"
    case t: Decl.Def                            => s"def ${t.name}"
    case t: Decl.Type                           => s"type ${t.name}"
    case _                                      => ""
  }

  private def packageName(ref: Term.Ref): String = spaces(ref.toString)

  private def visitNames(tree: Tree): String = tree match {
    case Source(stats) =>
      visitNamesStats(stats, visitNames, end = true)

    case Pkg(ref, stats) =>
      val name = packageName(ref)
      s"package $name." + nl +
        visitNamesStats(stats, visitNames)

    case pkgObj: Pkg.Object =>
      s"package object ${pkgObj.name}"

    case obj: Defn.Object =>
      s"object ${obj.name}"

    case cls: Defn.Class =>
      s"class ${cls.name}"

    case trt: Defn.Trait =>
      s"trait ${trt.name}"

    case imp: Import =>
      ""
    case e =>
      println(s"** Missing: ${e.getClass.toString} **")
      ""
  }
}
