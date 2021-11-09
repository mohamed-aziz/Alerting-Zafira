## Alerting Zafira

I made this projects 2 years ago for a company I used to work for, I had setup an ELK instance for them to aggregate logs for security reasons, And I made this small java program to monitor it.

Rules are written in groovy, you just use the elasticsearch query builder. Nothing complicated about it.

This projects monitors an elastic search instance for rules and sends notifications via telegram.

I have two examples scripts `succsessfulSSH.groovy` and `interestingCommands.groovy`, you can follow these examples to make your own scripts.