uint64 timeStamp              # Last update timestamp
uint32 locationId             # Unique location id
LatLng[] boundaries           # Location vertices (polygon)
float32 minimumAltitude       # Lower altitude bound of the location
float32 maximumAltitude       # Upper altitude bound of the location
uint8 locationType
    uint8 LT_NONE=0
    uint8 LT_SURFACE=1
    uint8 LT_AIR_SPACE=2
    uint8 LT_UNDERWATER=3
    uint8 LT_OIL_SPILL=4
string name                   # Location name
