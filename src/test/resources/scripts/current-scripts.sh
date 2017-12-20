curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSetElements.dataSet.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less


curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X" | jq . 

curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=id,code,periodType,categoryCombo,dataSetElements" | jq . 

curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements/cukQNo5wezt?fields=id,name,categoryCombo' |jq . 

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY |jq . |less

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categories\[id,code,name,categoryOptions\[id,name\]\]' |jq . |less

curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=id,code,periodType,categoryCombo" | jq 

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/kEEowBVuzrP?fields=id,name,categories\[id,code,name,categoryOptions\[id,code,name\]\]' |jq . 

## Districts (Organisation Units)
### From Bob
curl -u xxx:xxxx 'https://zim.dhis2.org/develop/api/organisationUnits.json?paging=false&filter=code:null:true&fields=name,id,code,parent\[name,code,id\]'

######will pull all the orgunits with no codes assigned as xml.  And

curl -u xxx:xxxx
'https://zim.dhis2.org/develop/api/organisationUnits.xml?paging=false&filter=code:!ilike:ZW&fields=name,id,code,parent\[name,code,id\]'

### will get ones with suspicious looking codes.  You could pipe into xslt to format nicely and group/sort by district.

### Alternatively using json and jq:

curl -u xxx:xxxx
'https://zim.dhis2.org/develop/api/organisationUnits.json?paging=false&filter=code:!ilike:ZW&fields=name,id,code,parent\[name,code,id\]'
| jq .organisationUnits |jq 'sort_by(.parent.code)'

and

curl -u xxx:xxxx 'https://zim.dhis2.org/develop/api/organisationUnits.json?paging=false&filter=code:!ilike:ZW&fields=name,id,code,parent\[name,code,id\]'
| jq .organisationUnits |jq 'sort_by(.parent.code)'
## End of Districts /Organization Units




## Critical
curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets/ZI7RVWq2o5X?fields=id,code,periodType,categoryCombo,dataSetElements\[categoryCombo,dataElement\[categoryCombo\]\]" | jq . 


## Critical (with code)
curl -u admin:district "https://zim.dhis2.org/develop/api/dataSets?filter=code:eq:ATB_005&fields=id,code,periodType,categoryCombo,dataSetElements\[categoryCombo,dataElement\[categoryCombo\]\]" | jq . 

### CategoryCombo with category options 
curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=id,name,categoryOptionCombos\[id,categoryOptions\[id,name,code\]\]' | jq .

curl -u admin:district 'https://zim.dhis2.org/develop/api/categoryCombos/Yqri7Qy4PhY?fields=categoryOptionCombos\[categoryOptions\[code\]\]' | jq .




