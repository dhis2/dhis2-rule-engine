package org.dhis2.ruleengine.exprk.internal

abstract class Function {
    abstract fun call(arguments: List<String>): String
}