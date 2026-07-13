package org.hisp.dhis.rules.models

/**
 * The target of an ASSIGN rule action, resolved by [RuleAction.assignTarget].
 *
 * The two targets differ in what they update during evaluation, not only in where the value
 * lands: a [Field] assignment produces a [RuleEffect] for the caller to apply, while a
 * [Variable] assignment updates the program rule variable so that rules evaluated afterwards
 * see the new value, and additionally produces a [RuleEffect] when the variable is backed by
 * a data element or tracked entity attribute.
 */
sealed class AssignTarget {
    /**
     * Assign to a program rule variable, referenced by name in the action's `content`.
     *
     * The engine updates the variable in the evaluation state, making the value visible to
     * the conditions and expressions of rules evaluated afterwards. When the variable is
     * backed by a data element or tracked entity attribute, a [RuleEffect] targeting the
     * backing field is also emitted — during event evaluation for data elements, during
     * enrollment evaluation for attributes — with the backing field and its attribute type
     * injected into a copy of the action. Calculated values are not backed by a field and
     * emit no effect.
     */
    data class Variable(val name: String) : AssignTarget()

    /**
     * Assign to the data element or tracked entity attribute named by the action's `field`.
     *
     * The engine emits a [RuleEffect] carrying the evaluated value; applying it is the
     * caller's responsibility. Program rule variables sourced from the assigned field are
     * not updated, so rules evaluated afterwards do not see the new value through them.
     */
    data class Field(val field: String) : AssignTarget()
}
