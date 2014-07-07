# ScalaSync #

## Build & run ##

```sh
```

## TODO ##

* network
* rename filename with womething better

## Contact ##

## Algorithm ##

compute a sum for each file
for each which are here before but where not there before
   push to the server (insert or update the deleted status to false)

for each which where here before and are not there anymore
    delete to the server (troggle dete to true)

get the list of all file from the server
  if the server file is not available locally get it
  if a file is there but not in the server delte it locally

