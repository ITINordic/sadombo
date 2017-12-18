curl -u admin:district 'https://zim.dhis2.org/develop/api/dataElements?filter=dataSetElements.dataSet.id:eq:ZI7RVWq2o5X&fields=id,name,categoryCombo' |jq . |less


