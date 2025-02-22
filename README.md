**Cron Parser CLI Tool - Specification**

**Overview**
The Cron Parser is a command-line tool designed to parse and evaluate cron expressions. It provides two key functionalities:
Compute the Next N Execution Times: Given a cron expression, the tool outputs the next 5 (or N) scheduled execution times.
Validate Execution for a Given Datetime: Given a cron expression and a datetime instance, the tool determines whether the cron should trigger at that time (returns true or false).
The parser supports standard cron syntax, including wildcards (*), step values (/), and ranges (-).

Cron Syntax Supported
The tool supports the standard five-field UNIX cron format:
<minute> <hour> <day_of_month> <month> <day_of_week>
Field
Allowed Values
Special Characters
Minute
0-59
*, ,, -, /
Hour
0-23
*, ,, -, /
Day of Month
1-31
*, ,, -, /
Month
1-12
*, ,, -, /
Day of Week
0-6 (Sunday = 0)
*, ,, -, /

Special Characters:
* → Matches all values in the field.
  , → Allows multiple values (e.g., 1,15 for the 1st and 15th day of the month).
- → Defines a range of values (e.g., 1-5 for Monday to Friday).
  / → Defines step values (e.g., */15 for every 15 minutes).

Features
1. Compute the Next N Execution Times
   Input: A valid cron expression and an optional integer N (default = 5).
   Output: The next N times the job is scheduled to run.
   Example:
   cron-parser "*/15 8-17 * * 1-5" 3
   Output:
   2025-02-21 08:00:00
   2025-02-21 08:15:00
   2025-02-21 08:30:00
2. Validate Execution for a Given Datetime
   Input: A valid cron expression and a datetime instance.
   Output: true if the job should run at that time, otherwise false.
   Example:
   cron-parser "*/30 9-17 * * 1-5" "2025-02-21 10:30:00"
   Output:
   true

Usage
cron-parser "<cron_expression>" [N]
cron-parser "<cron_expression>" "<datetime>"
Examples
Find Next 5 Execution Times
cron-parser "0 12 * * 1-5"
Output:
2025-02-24 12:00:00
2025-02-25 12:00:00
2025-02-26 12:00:00
2025-02-27 12:00:00
2025-02-28 12:00:00
Check if Cron Runs at a Specific Time
cron-parser "*/15 9-17 * * 1-5" "2025-02-21 14:45:00"
Output:
true

Implementation Details
Programming Language: Python
Libraries: datetime, argparse
Algorithm:
Parsing the Cron Expression: Convert fields into valid sets of numbers.
Compute Execution Times:
Start from the current timestamp.
Generate valid next occurrences based on cron rules.
Check Execution for Given Datetime:
Parse the given datetime.
Compare against the cron rules.

Error Handling
Invalid cron expressions should return an error message.
Invalid datetime formats should be handled gracefully.
Out-of-range values for fields should be reported as errors.

Future Enhancements
Support for named months (JAN, FEB, ...) and weekdays (MON, TUE, ...).
Add support for @yearly, @monthly, @weekly, @daily, and @hourly shorthand notations.
Extend the tool with a JSON output mode for better integration with other systems.

