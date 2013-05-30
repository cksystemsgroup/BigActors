uint64 taskStamp                # Last update timestamp in milliseconds since the Epoch (1970-01-01 00:00:00.000 UTC).
uint32 taskId                   # Unique task id
uint32 taskType                 # Type of the Task (Search, GotoWP, Track, GotoLocation )
    uint8 TT_NONE=0
    uint8 TT_SEARCH=1
    uint8 TT_GOTO_WP=2
    uint8 TT_TRACK=3
    uint8 TT_GOTO_LOCATION=4
    uint8 TT_RETURN_HOME=5
    uint8 TT_HOLD=6				# (wait in loiter)
    uint8 TT_VIDEOSTREAM=7
uint8 status                    # Status of the task
    uint8 TS_NONE=0
    uint8 TS_TODO=1             # Created and Not Assigned 
    uint8 TS_IN_PROGRESS=2      # Assigned and ack by vehicle  
    uint8 TS_DONE=3             # Done :)
    uint8 TS_ASSIGNED=4         # Assigned by task publisher not ack'd by vehicle  
    uint8 TS_CANCELED=5         # Canceled by task publisher
uint64 vehicleId                # Assigned or/and ack by vehicle  
uint64 creationStamp            # If later we adopt a dynamic task creation (in milliseconds since the Epoch).
string parameters               # JSON parameters related with task type