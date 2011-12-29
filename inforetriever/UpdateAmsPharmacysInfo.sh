#!/bin/bash

java -jar AmsPharmacysInfoRetriever.jar -a ./info/ams-farmacias/json | tee ./info/ams-farmacias/json/InfoRetrieverReport.txt


