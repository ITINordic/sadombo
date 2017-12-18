##Importing data - HTTP POST
curl -u admin:district -X POST -H "Content-Type: application/adx+xml" -d @data.xml "https://play.dhis2.org/demo/api/26/dataValueSets?dataElementIdScheme=code&orgUnitIdScheme=code"

##Retrieval
curl -u admin:district -H "Accept: application/adx+xml" "https://play.dhis2.org/demo/api/26/dataValueSets?dataValueSets?orgUnit=M_CLINIC&dataSet=MALARIA&period=201501"

## Live Examples
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo&pager=off' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categories\[name,categoryOptions\[id,name\]\]' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categories\[name,categoryOptions\]' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categories' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=name,categories' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq .dataElements {name} |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq .dataElements |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less
curl -u admin:district "https://zim.dhis2.org/develop/api/dataElements?filter=dataSets:.id:eq:ZI7RVWq2o5X" |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/categories?filter=dataSets.id:eq:ZI7RVWq2o5X' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo\[categories\[name\]\]' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo\[categories.name\]' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo\[categories\]' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo\[name\]' |jq . |less
curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo[name]' |jq . |less
curl -u admin:district "https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo[name]" |jq . |less
curl -u admin:district "https://zim.dhis2.org/develop/api/dataElements?filter=dataSets.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo" |jq .
curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=cate" |jq . |less
curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X"
curl -u admin:district -d @sample_data.xml -H "Content-type:application/adx+xml" "https://zim.dhis2.org/develop/api/dataValueSets.xml?idScheme=code&categoryOptionComboIdScheme=uid" |xmllint --format -


curl "https://play.dhis2.org/demo/api/26/dataValueSets?dataSet=pBOMPrpg1QX&period=201401&orgUnit=DiszpKrYNg8" -H "Accept:application/xml" -u admin:district -v


curl "https://play.dhis2.org/demo/api/26/dataValueSets?dataSet=pBOMPrpg1QX&dataSet=BfMAe6Itzgt&startDate=2013-01-01&endDate=2013-01-31&orgUnit=YuQRtpLP10I&orgUnit=vWbkYPRmKyS&children=true" -H "Accept:application/xml" -u admin:district -v

curl "https://play.dhis2.org/demo/api/26/dataValueSets?dataSet=pBOMPrpg1QX&orgUnit=DiszpKrYNg8&lastUpdatedDuration=10d" -H "Accept:application/xml" -u admin:district -v

### Miscellaneous
# /api/26/dataElementGroups.xml?links=false&paging=false

# /api/26/dataElements?query=anaemia

# /api/26/indicators.json?order=shortName:desc


## Get data elements with id property ID1 or ID2:
# /api/26/dataElements?filter=id:eq:ID1&filter=id:eq:ID2

## Get all data elements which has the dataSet with id ID1:
# /api/26/dataElements?filter=dataSetElements.dataSet.id:eq:ID1


##Other
# /api/26/dataElements.json?filter=id:in:[fbfJHSPpUQD,cYeuwXTCPkU]

## Get id and name on the indicators resource:
#/api/26/indicators?fields=id,name

## Get id and name from dataElements, and id and name from the dataSets on dataElements:
# /api/26/dataElements?fields=id,name,dataSets[id,name]

## Include all fields from dataSets except organisationUnits:
#/api/26/dataSets?fields=:all,!organisationUnits


## Example: Include only id, name and the collection of organisation units from a data set, but exclude the id from organisation units:
# /api/26/dataSets/BfMAe6Itzgt?fields=id,name,organisationUnits[:all,!id]






