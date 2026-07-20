package org.hisp.dhis.rules.models

/**
 * The target of an ASSIGN rule action, resolved by [RuleAction.assignTarget].
 *
 * The two valid targets differ in what they update during evaluation, not only in where the
 * value lands: a [Field] assignment produces a [RuleEffect] for the caller to apply, while a
 * [Variable] assignment updates the program rule variable so that rules evaluated afterwards
 * see the new value, and additionally produces a [RuleEffect] when the variable is backed by
 * a data element or tracked entity attribute.
 */
internal sealed class AssignTarget {
    /**
     * Assign to a program rule variable, referenced by name in the action's `content`.
     *
     * The engine updates the variable in the evaluation state, making the value visible to
     * the conditions and expressions of rules evaluated afterwards. When the variable is
     * backed by a data element or tracked entity attribute, a [RuleEffect] targeting the
     * backing field is also emitted, carrying a copy of the action with the backing field
     * and its attribute type injected. The copy resolves to [Field] through
     * [RuleAction.assignTarget] — matching how the effect is meant to be applied — and
     * compares unequal to the configured action.
     *
     * Data-element backed effects are emitted during event evaluation. Attribute backed
     * effects are emitted during enrollment evaluation when the execution has an enrollment
     * pass evaluating the rule, and otherwise (single-event evaluation, executions without
     * an enrollment, rules scoped to a program stage) during event evaluation, so the
     * assignment is never silently dropped. Calculated values are not backed by a field and
     * emit no effect. Assigning to a name that is not a defined program rule variable still
     * updates the evaluation state, but has no backing field, emits no effect and logs a
     * warning, as it may be a configuration mistake.
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

    /**
     * The action is an ASSIGN whose target cannot be resolved: it defines neither a `field`
     * nor a `content` value, or its `content` is not a program rule variable reference like
     * `#{variableName}`. The engine reports the assignment as an error effect; [reason]
     * describes what is wrong with the configuration.
     */
    data class Invalid(val reason: String) : AssignTarget()
}
