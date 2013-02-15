/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

/**
 * User: max
 * Date: 2/15/13
 * Time: 9:24 AM
 */

trait Monoid[T] {

  def append(m1: T, m2: T): T

  def zero: T
}