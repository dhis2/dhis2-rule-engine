### RuleEngine (WIP)

#### Initialization
`RuleEngine` is initialized in two steps.
 - Metadata configuration within `RuleEngineContext`
 - Setting background / contextual data for `RuleEngine`

```
RuleEngineContext ruleEngineContext = RuleEngineContext.builder(ruleExpressionEvaluator)
                .ruleVariables(ruleVariables)    // optional
                .rules(rules)                    // at least one rule must be supplied
                .build();
```

`.builder` factory method accepts instance of `RuleExpressionEvaluator` - something what knows how to evaluate program rule statements. `RuleExpressionEvaluator` implementation might be specific to certain platform. For example, on JVM it can be backed by [JEXL](http://commons.apache.org/proper/commons-jexl/), while on android it can be something like [duktape-android](https://github.com/square/duktape-android.git).

`RuleEngineContext` instance is immutable. It means it can be safely shared and reused across multiple threads. Next step will be setting some contextual data to the rule engine, which will be used as a source of data for most variables.

```
RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder()
        .enrollment(enrollment)    // contextual enrollment
        .events(events)            // contextual events
        .build();
```

`RuleEngine` is immutable as well. In example above, `toEngineBuilder()` method returns and instance of `RuleEngine.Builder` class. All parameters are optional, it means that one can simply call `ruleEngineContext.toEngineBuilder().build()` to get an instance of engine back.

#### Evaluation
Now we can send target event or enrollment to the engine in order to get some `RuleEffect`s back. Before showing code, there are certain quirks which one should be aware of. You are not allowed to send duplicate events or enrollments to the engine as evaluation targets. In other words, if you have already supplied enrollment or event as a part of the contextual data, you won't be allowed to send it again as evaluation target. For example:

```
RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder()
        .enrollment(enrollment)    // contextual enrollment        
        .build();

ruleEngine.evaluate(enrollment);   // not allowed!        
```

In general, there are a few scenarios in which rule engine can be used. Let's use next notation to declare possible options.

`<metadata>(contextual_data)[evaluation_target]`

- `<rules, variables>(single_events - target_event)[target_event]`: applicable for programs without registration.
- `<rules, variables>(enrollment, enrollment_events - target_event)[target_event]`: applicable for programs with registration. In this case, event is under evaluation.
- `<rules, variables>(enrollment_events)[target_enrollment]`: evaluating enrollment. In this case, events which are a part of enrollment, can be used as source of values for program rule variables.  

There are two methods for evaluation at the moment:
```
List<RuleEffect> enrollmentEffects = ruleEngine.evaluate(enrollment);
List<RuleEffect> eventEffects = ruleEngine.evaluate(event);
```

List of supported environment (contextual) variables:
 - current_date
 - event_date
 - event_count
 - due_date
 - event_id
 - enrollment_date
 - enrollment_id
 - enrollment_count
 - incident_date
 - tei_count

### Development
This library implements the semantic release setup, which means that version numbers are not manually maintained but
derived from the commit/PR history.

Branches:
- `main`: a push to `main` branch will trigger a new production release (both Maven and NPMJS).
- `beta`: a push to `beta` branch will trigger a SNAPSHOT release in Maven and a new beta release in NPMJS.

Version number are determined by the presence of commits with these suffixes:
- `fix:`: it will increase the patch number.
- `feat:`: it will increase the minor version number.
- `feat!:`: it will increase the major version number.

If there is not any commit with any of this tags between the previous version and the current commit, nothing will be published.

Typical workflow:
1. Do work in a feature branch. There is no need to add tags to the commits.
2. Create a PR to `beta` branch including a tag in the PR title depending on the kind of changes.
3. Merge the PR using **Squash and merge**. It will publish a SNAPSHOT/BETA release if there is a version change.
4. Create a PR to `main` branch. Once merged, it will publish a production release.


---
WIP
