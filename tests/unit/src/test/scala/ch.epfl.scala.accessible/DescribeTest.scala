// format: off

package ch.epfl.scala.accessible

import scala.meta._

object DescribeTests extends DescribeTestsUtils {
  
  // Lit
  // check[Lit]                ("1")

  // Term
  {
    import Term._
    check[This]("this", "this")
  }

  // Type
  {
    import Type._

    checkType[Name]               ("B"                             , "B")
    checkType[Select]             ("a.B"                           , "select a B")
    checkType[Project]            ("a#B"                           , "project a B")
    checkType[Singleton]          ("this.type"                     , "singleton this")
    checkType[Singleton]          ("t.type"                        , "singleton t")
    checkType[Apply]              ("F[T]"                          , "F of T")
    checkType[Apply]              ("Map[K, V]"                     , "Map applied to K, V")
    checkType[Apply]              ("M[_, _]"                       , "M taking 2 parameters")
    checkType[ApplyInfix]         ("K Map V"                       , "K Map V")
    checkType[Function]           ("() => B"                       , "function Unit to B")
    checkType[Function]           ("A => B"                        , "function A to B")
    checkType[Function]           ("(A, B) => C"                   , "function A, B to C")
    checkType[ImplicitFunction]   ("implicit A => B"               , "implicit function A to B", dialect = dotty)
    checkType[Tuple]              ("(A, B)"                        , "tuple 2 of A, B")
    checkType[With]               ("A with B"                      , "A with B")
    checkType[And]                ("A & B"                         , "A and B", dialect = dotty)
    checkType[Or]                 ("A | B"                         , "A or B", dialect = dotty)
    checkType[Refine]             ("A { def f: B }"                , "type refinements: A def f returns: B")
    checkType[Refine]             ("A { }"                         , "type refinements: A")
    checkType[Refine]             ("{ def f: B }"                  , "type refinements: def f returns: B")
    // checkType[Existential     ]("A forSome { type T }"          , "")
    // checkType[Annotate        ]("T @A"                          , "")
    checkType[Lambda]             ("[X] => (X, X)"                 , "lambda X to tuple 2 of X, X", dialect = dotty)
    checkType[Placeholder]        ("_"                             , "placeholder")
    checkType[Bounds]             ("_ >: A <: B"                   , "placeholder super-type of: A sub-type of: B")
    checkType[Bounds]             ("_ <: B"                        , "placeholder sub-type of: B")
    checkType[Bounds]             ("_ >: A"                        , "placeholder super-type of: A")
    checkType[ByName]             ( "=> T"                         , "by name: T")
    checkType[Repeated]           ("Any*"                          , "repeated: Any")
    check[Param]                  ("def f[→A <% B[A]←]: C"         , "A view bounded by: B of A.")
    check[Param]                  ("def f[→A: B←]: C"              , "A context bounded by: B.")
    check[Param]                  ("def f[→A : B : C←]: D"         , "A context bounded by: B, C.")
  }

  // Pat

  // Decl
  {
    import Decl._
    check[Def]("def f: String", "def f returns: String")
  }

  // Defn
  {
    import Defn._
    check[Class]("class A"           , "class A")
    check[Class]("class A[T]"        , "class A parametrized with: T")
    check[Def]  ("def f: String = 1" , "def f returns: String body: 1")
  }

  // Mod
  {
    import Mod._
    check[Abstract]     ("abstract class A"   , "abstract class A")
    // check[Annot]     ("@tailrec def f = 1" , "at tailrec def f = 1")
    check[Contravariant]("class A[→-T←]"      , "contra-variant T")
    check[Covariant]    ("class A[→+T←]"      , "co-variant T")

    check[Private]      ("→private← class A"          , "private")
    check[Private]      ("→private[scope]← class A"   , "private within scope")
    check[Private]      ("→private[this]← class A"    , "private this")
    check[Protected]    ("→protected← class A"        , "protected")
    check[Protected]    ("→protected[scope]← class A" , "protected within scope")
    check[Protected]    ("→protected[this]← class A"  , "protected this")
  }

  // Enumerator

  // Ctor

  // Import

  // Misc
}
