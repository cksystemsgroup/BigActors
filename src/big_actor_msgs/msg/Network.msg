uint64 timeStamp              # Last update timestamp in milliseconds since the Epoch (1970-01-01 00:00:00.000 UTC).
uint8 type                    # Network type (Physical Layer).
	uint8 NT_NONE=0
	uint8 NT_ETHERNET_10M=1
	uint8 NT_ETHERNET_100M=2
	uint8 NT_ETHERNET_1G=3
	uint8 NT_ETHERNET_10G=4
	uint8 NT_WIFI_2MB=10
	uint8 NT_WIFI_11MB=11
	uint8 NT_WIFI_27MB=12
	uint8 NT_WIFI_54MB=13
	uint8 NT_WIFI_600MB=14
	uint8 NT_WIFI_1300MB=15
	uint8 NT_WIFI_7000MB=16
	uint8 NT_MESH_500kB=20
	uint8 NT_ACOUSTIC_7kB=30
	uint8 NT_ACOUSTIC_9kB=31
	uint8 NT_ACOUSTIC_14kB=32
	uint8 NT_ACOUSTIC_31kB=33
    uint8 NT_PICCOLO=40
    uint8 NT_AIS=50
uint32 address                # TCP/IP network address, Piccolo channel number, ...
uint32 mask                   # TCP/IP network mask or zero if not applicable