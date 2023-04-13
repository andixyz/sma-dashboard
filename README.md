# SMA Dashboard

Collect data from SMA inverter and SMA Home Manager 2.0. Visualize the data in a Grafana dashboard.

## Environment

- SUNNY TRIPOWER 8.0 SE
- Sunny Home Manager 2.0

## Build

[Maven](https://maven.apache.org/) should be installed.

Ensure that docker engine is running on your local machine.

    mvn clean install
    docker compose build

## Setup and run

Change .env file or copy it to .env.xyz.

In the .env.xyz file change the following parameters:

    SMA_WEBUI_PASSWD=changeme
    SMA_WEBUI_HOST=xxx.xxx.xxx.xxx

SMA_WEBUI_PASSWD is the password you are using to login into your SMA inverter web frontend with role "User".

SMA_WEBUI_HOST specifies the IP address to your SMA inverter.

    docker compose --env-file ./.env.xyz up -d 

Access InfluxDB by calling (see .env file for username and password)

http://localhost:8086

Access Grafana by calling

http://localhost:3000


## Web UI Adapter

Actual supporting [measures](https://github.com/andixyz/sma-dashboard/blob/main/adapter/sma-webui-adapter/src/main/resources/application.properties):

| ID            | Code                    | Name DE                       | Name EN |
|---------------|-------------------------|-------------------------------|---------|
| 6380_40251E00 | DcMs.Watt               | DC Leistung Eingang [A]/[B]   |         |
| 6100_0046C200 | PvGen.PvW               | Leistung PV Werkzeug          |         |
| 6100_00496900 | BatChrg.CurBatCha       | Momentane Batterieladung      |         |
| 6100_00496A00 | BatDsch.CurBatDch       | Momentane Batterieentladung   |         |
| 6100_00295A00 | Bat.ChaStt              | Aktueller Batterieladezustand |         |
| 6100_40463700 | Metering.GridMs.TotWIn  | Leistung Bezug                |         |
| 6100_40463600 | Metering.GridMs.TotWOut | Leistung Einspeisung          |         |
| 6100_40263F00 | GridMs.TotW             | Leistung / Verbrauch          |         |

The adapter uses the Code to store the values into the InfluxDB.

## SMA Home Manager 2.0 UDP adapter

### node-red

Installed packages

* node-red-contrib-buffer-parse
* node-red-dashboard

### Wireshark

    (ip.addr == 192.168.178.142 || ip.addr == 192.168.178.146) && udp

### Sources

* https://nerdiy.de/en/read-out-howto-node-red-sma-sunny-home-manager-data/
* https://nerdiy.de/de_de/howto-node-red-sma-sunny-home-manager-daten-auslesen/
* https://github.com/Nerdiyde/NodeRedSnippets