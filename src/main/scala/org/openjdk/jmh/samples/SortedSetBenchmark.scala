/**
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.jmh.samples

import org.openjdk.jmh.annotations._

import scala.collection.immutable.SortedSet
import scala.util.Random

@State(Scope.Thread)
class SortedSetBenchmark {

  import SortedSetBenchmark._

  private var items = SortedSet((1 to 10000).map(i => Item(i.toString, i % 10)):_ *)

  @Benchmark
  def filterNot(): SortedSet[Item] = {
    val id = Random.nextInt(Count)
    val idString = id.toString
    items = items.filterNot(_.id == idString)
    items += Item(idString, id % 10)
    items
  }

  @Benchmark
  def minus(): SortedSet[Item] = {
    val id = Random.nextInt(Count)
    val item = Item(id.toString, id % 10)
    items -= item
    items += item
    items
  }

  @Benchmark
  def find(): Option[Item] = {
    items.find(_.id == Random.nextInt(Count).toString)
  }

  @Benchmark
  def noop(): Unit = {
    // ...
  }

}

object SortedSetBenchmark {

  val Count = 10000

  case class Item(id: String, priority: Int)

  implicit val ItemOrdering: Ordering[Item] = Ordering.by(item => (item.priority, item.id))

}