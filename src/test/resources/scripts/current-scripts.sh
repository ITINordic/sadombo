curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSetElements.dataSet.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less


curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X" | jq . 

curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=id,code,periodType,categoryCombo,dataSetElements" | jq . 

curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements/cukQNo5wezt?fields=id,name,categoryCombo' |jq . 

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY |jq . |less

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categories\[id,code,name,categoryOptions\[id,name\]\]' |jq . |less

curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=id,code,periodType,categoryCombo" | jq 

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/kEEowBVuzrP?fields=id,name,categories\[id,code,name,categoryOptions\[id,code,name\]\]' |jq . 

## Critical
curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=id,code,periodType,categoryCombo,dataSetElements\[categoryCombo,dataElement\[categoryCombo\]\]" | jq . 


## Critical (with code)
curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets?filter=code:eq:ATB_005&fields=id,code,periodType,categoryCombo,dataSetElements\[categoryCombo,dataElement\[categoryCombo\]\]" | jq . 

### CategoryCombo with category options 
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categoryOptionCombos\[id,categoryOptions\[id,name,code\]\]' | jq .

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=categoryOptionCombos\[categoryOptions\[code\]\]' | jq .




