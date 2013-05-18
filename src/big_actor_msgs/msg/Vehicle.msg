uint64 timeStamp              # Last update timestamp
uint64 vehicleId              # Unique vehicle id
LatLngAlt position            # Vehicle position
float32 heading               # Vehicle heading
uint32 taskId                 # Currently processed task
uint8 taskState               # see Task.status
uint8 vehicleState            # Status of the vehicle
    uint8 VS_NONE=0
    uint8 VS_IDLE=1           # Vehicle idles
    uint8 VS_BUSY=2           # Vehicle is working at task specified in taskId
uint8 vehicleType
    uint8 VT_NONE=0
    uint8 VT_QUADROTOR=1
    uint8 VT_VESSEL=2 
    uint8 VT_DRIFTER=3
    uint8 VT_MODEL_AIRPLANE=4