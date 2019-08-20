package org.hisp.dhis.rules.models

abstract class RuleActionMessage : RuleAction() {

    abstract var content: String?

    abstract var data: String?

    abstract var field: String?
}
