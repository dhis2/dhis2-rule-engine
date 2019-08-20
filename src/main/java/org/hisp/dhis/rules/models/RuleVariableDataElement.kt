package org.hisp.dhis.rules.models

abstract class RuleVariableDataElement(var rName: String?,
                                       var dataElement: String?,
                                       var dataElementType: RuleValueType?) : RuleVariable(rName)
