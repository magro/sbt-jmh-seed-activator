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

import java.util
import java.util.{HashMap => JHashMap}

import org.openjdk.jmh.annotations._

import scala.collection.JavaConverters._
import scala.collection.immutable.HashMap
import scala.collection.immutable.ListMap
import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.util.Random

@State(Scope.Thread)
class MapBuilderBenchmark {

  import MapBuilderBenchmark._

  private var items = new util.ArrayList[Item]((1 to Count).map(i => Item("LM_L_" + i.toString, Random.nextLong())).asJava)

  @Benchmark
  def arrayToListMapApply(): Unit = {
    val res = ListMap(items.toArray(new Array[Item](items.size())).map(i => i.processId -> i.logicalTime): _*)
    res.size
  }

  @Benchmark
  def foldIteratorMapUpdate(): Unit = {
    val res = items.iterator().asScala.foldLeft(Map.empty[String, Long]) {
      case (result, item) => result.updated(item.processId, item.logicalTime)
    }
    res.size
  }

  @Benchmark
  def foldIteratorTreeMapUpdate(): Unit = {
    val res = items.iterator().asScala.foldLeft(TreeMap.empty[String, Long]) {
      case (result, item) => result.updated(item.processId, item.logicalTime)
    }
    res.size
  }

  @Benchmark
  def foldIteratorHashMapUpdate(): Unit = {
    val res = items.iterator().asScala.foldLeft(HashMap.empty[String, Long]) {
      case (result, item) => result.updated(item.processId, item.logicalTime)
    }
    res.size
  }

  @Benchmark
  def foldIteratorListMapUpdate(): Unit = {
    val res = items.iterator().asScala.foldLeft(ListMap.empty[String, Long]) {
      case (result, item) => result.updated(item.processId, item.logicalTime)
    }
    res.size
  }

  @Benchmark
  def foldIteratorWithMapBuilder(): Unit = {
    val res = items.iterator().asScala.foldLeft(Map.newBuilder[String, Long]) {
      case (result, item) => result += (item.processId -> item.logicalTime)
    }.result()
    res.size
  }

  @Benchmark
  def foldIteratorWithTreeMapBuilder(): Unit = {
    val res = items.iterator().asScala.foldLeft(TreeMap.newBuilder[String, Long]) {
      case (result, item) => result += (item.processId -> item.logicalTime)
    }.result()
    res.size
  }

  @Benchmark
  def foldIteratorWithHashMapBuilder(): Unit = {
    val res = items.iterator().asScala.foldLeft(HashMap.newBuilder[String, Long]) {
      case (result, item) => result += (item.processId -> item.logicalTime)
    }.result()
    res.size
  }

  @Benchmark
  def foldIteratorWithListMapBuilder(): Unit = {
    val res = items.iterator().asScala.foldLeft(ListMap.newBuilder[String, Long]) {
      case (result, item) => result += (item.processId -> item.logicalTime)
    }.result()
    res.size
  }

  @Benchmark
  def foldIteratorWithMutableMapBuilder(): Unit = {
    val res = items.iterator().asScala.foldLeft(mutable.Map.newBuilder[String, Long]) {
      case (result, item) => result += (item.processId -> item.logicalTime)
    }.result()
    res.size
  }

  @Benchmark
  def foldIteratorWithMutableListMapBuilder(): Unit = {
    val res = items.iterator().asScala.foldLeft(mutable.ListMap.newBuilder[String, Long]) {
      case (result, item) => result += (item.processId -> item.logicalTime)
    }.result()
    res.size
  }

  @Benchmark
  def foldIteratorWithJavaHashMap(): Unit = {
    val res = items.iterator().asScala.foldLeft(new JHashMap[String, Long](items.size() + 1)) {
      case (result, item) =>
        result.put(item.processId, item.logicalTime)
        result
    }
    res.size
  }

  @Benchmark
  def imperativeIteratorWithJavaHashMap(): Unit = {
    val result = new JHashMap[String, Long](items.size() + 1)
    val iter = items.iterator()
    while(iter.hasNext) {
      val item = iter.next()
      result.put(item.processId, item.logicalTime)
    }
    result.size
  }

}

object MapBuilderBenchmark {

  val Count = 5

  case class Item(processId: String, logicalTime: Long)

}