# Silver Bars — Live Orders Board
A simple Live Orders Board for Silver Bars

## Assumptions/Decisions
* userId is probably a foreign key into Users and should be a Long for simplicity
* Eventual consistency is okay (ConcurrentHashMap might return cached data)

## Not completed
* Add ability to configure (e.g. port and interface currently hard-coded)
* Add logging
— access
— debug/info
* Add scalastyle/wartremover

## Instructions
* Start server with:
```
$ sbt run
```
