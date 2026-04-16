package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.utils.filterRules
import org.hisp.dhis.rules.utils.mergeSorted

data class RuleEngineContext(
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable> = emptyList(),
    val ruleSupplementaryData: RuleSupplementaryData = RuleSupplementaryData(),
    val constantsValues: Map<String, String> = emptyMap(),
) {
    // Rules that apply to enrollments and to events with no stage-specific override,
    // sorted by priority. Computed once so evaluate() calls pay no per-call cost.
    internal val enrollmentRules: List<Rule> = filterRules(rules).sorted()

    // For each program stage: the merged, sorted list of enrollment rules + stage rules.
    // enrollmentRules is already sorted; stage rules are sorted separately and then merged
    // in O(n+m) rather than re-sorting the full combined list from scratch.
    // Events whose stage has no entry fall back to enrollmentRules.
    internal val rulesByStage: Map<String, List<Rule>> = rules
        .filter { !it.programStage.isNullOrEmpty() }
        .groupBy { it.programStage!! }
        .mapValues { (_, stageRules) -> mergeSorted(enrollmentRules, stageRules.sorted()) }
}
