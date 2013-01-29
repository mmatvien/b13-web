/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */
package persistence

sealed trait Permission
case object Administrator extends Permission
case object NormalUser extends Permission
