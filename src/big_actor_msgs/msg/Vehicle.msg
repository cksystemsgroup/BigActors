uint64 vehicleId
LatLngAlt position
float32 heading
uint32 taskId
uint8 taskState         # see Task.status
uint8 vehicleState
    uint8 VS_NONE=0
    uint8 VS_IDLE=1
    uint8 VS_BUSY=2
uint64 timeStamp