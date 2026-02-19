# Athena User Guide

Athena is a task management taskbot that helps users keep track of todos, deadlines and event type tasks. It is named after the greek goddess of wisdom, Athena.

<img width="300" height="500" alt="Screenshot 2026-02-18 145922" src="https://github.com/user-attachments/assets/e95ee645-dd9a-4089-9e0a-44e6ea711aae" />

## Features
- **Todo tasks:** Simple tasks that have no deadline or timeframe
- **Event tasks:** Tasks with a start and end time
- **Deadline tasks:** Tasks with a deadline
- **Mark/Unmark:** Tracks status of tasks
- **Delete:** Deletes task from the chatbot
- **Find:** Search for a task using a keyword
- **Reminders:** Reminders for tasks appear on launch of app


## Usage
Type commands in the input box at the bottom of the window and press enter or click send

### Adding tasks

**Todo:**

```
todo read book
```

**Deadline:**

```
deadline return book /by 2026-02-25 2359
```

**Note:** Time input is optional

**Event:**

```
event project meeting /from 2026-02-25 2100 /to 2026-02-25 2200
```

**Note:** Time input is optional

### Managing Tasks
**List:**

```
list
```

**Mark:**

```
mark 1
```

**Unmark:**

```
unmark 1
```

**Find:**

```
Find book
```

**Delete:**

```
Delete 3
```


### Other features

**Reminders:**

No input is required for this. Reminders for tasks due or occuring in the next 7 days appear when app is launched.

### Closing the app

```Bye``` or ```bye``` inputs will close the app. Alternatively closing the window manually also works.

## Command Summary

| **Command** | **Format** | **Example**  |
|-------------|------------|--------------|
| Add Todo task | `todo [DESCRIPTION]` | `todo read book` |
| Add Event task   | `event [DESCRIPTION] /from [DATE] [TIME](optional) /to [DATE] [TIME](optional)` | `event project meeting /from 2026-02-25 2100 /to 2026-02-25 2200` |
| Add deadline task | `deadline [DESCRIPTION] /by [DATE] [TIME](optional)` | `deadline return book /by 2026-02-24` |
| List tasks | `list` |  -  |
| Mark task | `mark [INDEX]` | `mark 1` |
| Unmark task | `unmark [INDEX]` | `unmark 1` |
| Find task | `find [KEYWORD]` | `find book` |
| Delete task | `delete [INDEX]` | `delete 2` |
| Exit | `Bye` or `bye ` |  -  |

## Formats of dates accepted
- ```yyyy-MM-dd HHmm```
- ```yyyy-MM-dd```
