### RuleEngine (WIP)  
Core rules contains `RuleEngine` which takes contextual data in the form of `Rules` and `RuleVariables`. It then 
evaluates rule expression based on provided implementation of `ExpressionEvaluator` and returns `RuleActions`.
