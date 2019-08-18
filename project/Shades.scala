import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import xml.{NodeSeq, Node => XNode, Elem}
import xml.transform.{RuleTransformer, RewriteRule}

object Shades {
  val shadePrefix = "sbt.internal.shaded"
  val Shade = config("shade").hide

  def jlineShadeSettings: Seq[Setting[_]] =
    inConfig(Shade)(
      Defaults.configSettings ++
      baseAssemblySettings ++ Seq(
      // assembly / logLevel := Level.Debug,
      assembly / assemblyShadeRules := Seq(
        ShadeRule.rename("org.fusesource.**" -> s"$shadePrefix.@0").inAll,
        ShadeRule.rename("jline.**"          -> s"$shadePrefix.@0").inAll,
      ),
      assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeBin = false, includeScala = false),
      // cut ties with Runtime
      assembly / fullClasspath := (Shade / fullClasspath).value,
      // cut ties with Runtime
      assembly / externalDependencyClasspath := (Shade / externalDependencyClasspath).value,
      // cut ties with Runtime
      assembly / mainClass := mainClass.value,
      // cut ties with Runtime
      assembly / test := {}
    )) ++ Seq(
      Compile / packageBin := (Shade / assembly).value
    )

  def dependenciesFilter(n: XNode) = new RuleTransformer(new RewriteRule {
    override def transform(n: XNode): NodeSeq = n match {
      case e: Elem if e.label == "dependencies" => NodeSeq.Empty
      case other => other
    }
  }).transform(n).head
}
