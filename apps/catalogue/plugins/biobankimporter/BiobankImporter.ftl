<#macro BiobankImporter screen>

<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${screen.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
	
	<input type="hidden" name="__dataTypeCount">
	
<!-- this shows a title and border -->
	<div class="formscreen">
		<div class="form_header" id="${screen.getName()}">
		${screen.label}
		</div>
		
		<#--optional: mechanism to show messages-->
		<#list screen.getMessages() as message>
			<#if message.success>
		<p class="successmessage">${message.text}</p>
			<#else>
		<p class="errormessage">${message.text}</p>
			</#if>
		</#list>
		
		<div class="screenbody">
		<script type="text/javascript">
			HashMap = function(){
			  this._dict = [];
			}
			HashMap.prototype._get = function(key){
			  for(var i=0, couplet; couplet = this._dict[i]; i++){
			    if(couplet[0] === key){
			      return couplet;
			    }
			  }
			}
			HashMap.prototype.put = function(key, value){
			  var couplet = this._get(key);
			  if(couplet){
			    couplet[1] = value;
			  }else{
			    this._dict.push([key, value]);
			  }
			  return this; // for chaining
			}
			HashMap.prototype.get = function(key){
			  var couplet = this._get(key);
			  if(couplet){
			    return couplet[1];
			  }
			}
		</script>
		<script type="text/javascript">
			
			var dataTypeOptions = new Array();
			var fieldName = "";
			var fieldNameOptions = new Array();
			var map = new HashMap();
			var count = 0;
			var index = 1;
			var headerCount = 0;
			
			function changeFieldContent(columnName)
			{
			
				var classType = document.getElementsByName(columnName);
				
				if(classType[1].value.toString() == "Measurement:dataType")
				{	
					makeTable(columnName);
				}else{
					destroyTable(columnName);
				}
				
				if(classType[0].value.toString() == "ObservedValue"){
					dynamicOption(columnName, "ObservedValue");
				}else if(classType[0].value.toString() == "NULL"){
					dynamicOption(columnName, "NULL");
				}else{
					resumeTheOptions(columnName)
				}
			}
			
			function dynamicOption(columnName, changedContext){
				
				var select = document.getElementsByName(columnName);
					
				select = select[1];
					
				var newOption = document.createElement('option');
					
				newOption.innerHTML = changedContext;
					
				var length = select.length;
					
				for(var i = 0; i < length; i++){
					select.options[0] = null;
				}
					
				select.add(newOption, 0);
			}
			
			function resumeTheOptions(columnName){
				
				var select = document.getElementsByName(columnName);
					
				select = select[1];
					
				if(select.length != fieldNameOptions.length)
				{
					select.options[0] = null;
					
					var option = document.createElement('option');
						
					for(var i = 0; i < fieldNameOptions.length; i++)
					{	
						var option = document.createElement('option');
						option.innerHTML = fieldNameOptions[i];
						select.add(option, i);
					}
				}
				
			}
			
			function destroyTable(id) {

				document.getElementById(id).innerHTML = "data type";
			}
			
			function greeting(){

				var table = document.getElementById(fieldName);
				makeTable(fieldName);
				
			}
			
			function makeTable(id) {
				
				var tab;
				
				tab = document.createElement('table');
				tab.setAttribute('id',id);
				tab.setAttribute('border','1');
				tableExisting = false;
					
				var row = new Array();
				var cell = new Array();
				
				row[0]=document.createElement('tr');
				
				cell[0]=document.createElement('td');
				
				cell[1]=document.createElement('td');
				
				cell[2]=document.createElement('td');
				
				var selection = document.createElement('select');
				
				selection.setAttribute("name", id + "_options_" + count);
				
				for(var index = 0; index < dataTypeOptions.length; index++)
				{
					var option = document.createElement('option');
					
					option.innerHTML = dataTypeOptions[index];
				
					selection.appendChild(option);
				}
				
				var textInput=document.createElement('input');
				
				textInput.setAttribute('type','text');
				
				textInput.setAttribute("name", id + "_input_" + count);
				
				textInput.setAttribute('size','15');
				
				var addButton = document.createElement('button');
				
				addButton.setAttribute('name','add');
				
				addButton.setAttribute('type','button');
				
				addButton.innerHTML = "add";
				
				fieldName = id;
				
				addButton.setAttribute("onclick", "greeting();");
				
				cell[0].appendChild(selection);
				
				cell[1].appendChild(textInput);
				
				cell[2].appendChild(addButton);
				
				row[0].appendChild(cell[0]);
				
				row[0].appendChild(cell[1]);
				
				row[0].appendChild(cell[2]);
				
				tab.appendChild(row[0]);
				
				var oldTable = document.getElementById('table');
				
				var cellActivated = document.getElementsByName(id);
				
				var cell = cellActivated[3];
				
				document.getElementById(id).appendChild(tab);
				
				count++;
				
				document.getElementsByName('__dataTypeCount')[0].value = count;
				
				return (count - 1);
			}
			
			function createSelection(option)
			{
				
				if(contains(option, dataTypeOptions) == false){
					
					dataTypeOptions.push(option);
				}
			}
			
			function createFieldName(option)
			{
				if(contains(option, fieldNameOptions) == false){
					fieldNameOptions.push(option);
				}
			}
			
			function contains(option, array){
				
				for(var i = 0; i < array.length; i++)
				{
					if(array[i] == option)
					{
						
						return true;
					}		
				}
				return false;	
			}
			
			function updateTableContent(option, array){
				
				
				var classType = document.getElementById("shortcutClassType");
				var fieldName = document.getElementById("shortcutFieldName");
				var enterIndex = document.getElementById("shortcut");
				var chosenTarget = document.getElementById("shortcutTarget");
				
				if(enterIndex.value != "")
				{
					var arrayIndex = enterIndex.value.split(";");
					
					for(var i = 0; i < arrayIndex.length; i++)
					{
						
						var indexRange = arrayIndex[i].split(">");
						
						var max = indexRange[1];
						
						if(max == "n")
						{	
							max = index;
						}else{
						
							max++;
						
						}
						
						var min = indexRange[0];
						
						if(indexRange.length > 1)
						{
							
							for(var j = min; j < max;j++){
								
								var value = map.get(parseInt(j));
								var multipleCells = document.getElementsByName(value);
								multipleCells[0].value = classType.value;
								multipleCells[2].value = chosenTarget.value;
								changeFieldContent(value);
								multipleCells[1].value = fieldName.value;
								
							}
						}else{
							
							var value = map.get(parseInt(arrayIndex[i]));
							var multipleCells = document.getElementsByName(value);
							multipleCells[0].value = classType.value;
							multipleCells[2].value = chosenTarget.value;
							changeFieldContent(value);
							multipleCells[1].value = fieldName.value;
						}
					}
					
				}else{
						
					alert("Please fill in all the information on the shortcut");
						
				}
			}
			
			
			function createHashMap(element)
			{
				map.put(index, element);
				index++;
			}
			
			function getCount(id)
			{
				
				headerCount++;
				var div = document.getElementById(id);
				div.innerHTML = "The column index is " + headerCount;
			}
			
			function readInMappingFile(header, fieldValue, rowIndex, colIndex){
				
				var select = document.getElementsByName(header);
				
				rowIndex = rowIndex - 1;
				
				select = select[rowIndex];
				
				if(fieldValue != ""){
					
					if(rowIndex == 3){
						
						
						
						if(fieldValue.toLowerCase() === "true"){
							select.checked = true;
						}else{
							select.checked = false;
						}
						
					}else if(rowIndex > 3){
						
						var localCount = makeTable(header);
						
						var molgenisDataType = document.getElementsByName(header + "_options_" + localCount);
						
						var inputOriginaldataType = document.getElementsByName(header + "_input_" + localCount);
						
						var array = fieldValue.split(";");
						
						molgenisDataType[0].value = array[0];
						
						inputOriginaldataType[0].value = array[1];
						
					}else{
						select.value = fieldValue;
					}
				}
			}
			
			function insertInvestigation(investigationName){
				
				if(investigationName != null){
				
					document.getElementsByName('investigation')[0].value = investigationName;
				}
			}
			
			function checkInvestigation(){
			
				var investigationValue = document.getElementById("investigation").value;
				
				if(investigationValue == ""){
					alert("The investigation cannot be null!");
					return false;
				}else{
					return true;
				}
			}
			
			function checkMultipleSheets(){
				
				var whetherProtocol = document.getElementById("whetherProtocol");
				if(whetherProtocol.style.display == "block") {
			    		whetherProtocol.style.display = "none";
			  	}else {
					whetherProtocol.style.display = "block";
				}
			
			}
		</script>
			<div class="screenpadding" id = "screenpadding">	
			    <h3 id="test"> Import your data to pheno model  </h3>
		        
		        <div id="fileChosenSection">
			        <#if screen.isImportingFinished() == true>
				        <h4> 1.Please select the file </h4>
				        <input type="file" name = "uploadFile"/><br /><br />
				        
				        <h4> 2.Please input a starting row index for importing data </h4>
				        <input type="text" id="startingRowIndex" name="startingRowIndex" size=5 value=1 />
				        In some excel, the actual data do not start with the first column
				        <h4> 3.Does the input file have multiple sheets? 
				        <input type="checkbox" id="multipleSheets" name="multipleSheets" onclick="checkMultipleSheets()"/></h4>
				        <div id="whetherProtocol" style="display: none">
				        	3.a Would you like to import each sheet as protocol name?<input type="checkbox" id="sheetImportProtocol" name="sheetImportProtocol"/>
				        </div>
				        <h4> 4.Please choose ONE of the following options: </h4>
				        
				        <input type="submit" value="ImportByColumnHeader" onclick="__action.value='UploadFileByColumn';return true;"/>
				        <input type="submit" value="ImportByRowHeader" onclick="__action.value='UploadFileByRow';return true;"/>
				        
				        <!-- <input type="submit" value="Empty Database" onclick="__action.value='fillinDatabase';return true;"/>-->
	 				
					</#if>
					<#list screen.getDataTypeOptions() as dataTypeOptions>
						<script type="text/javascript">
							createSelection("${dataTypeOptions}");
						</script>
					</#list>
					
					
					<#list screen.getSpreadSheetHeanders() as header>
						<script type="text/javascript">
							createHashMap("${header}");
						</script>
					</#list>
					
					<#list screen.getChooseFieldName() as fieldNameOptions>
						<script type="text/javascript">
							createFieldName("${fieldNameOptions}");
						</script>
					</#list>
				</div>
				
				<div id="mappingFileSection">
					<#if screen.isImportingFinished() == false>
						Please enter your investigation: <input type="text" id="investigation" name="investigation" size="15" value="${screen.getInvestigationName()}"> <br/><br/>
						
						
						Enter column numbers </br></br>  
						<input type="text" id="shortcut" size="15" value=""> 
						<select id='shortcutClassType' name='shortcut' onchange="changeFieldContent('shortcut');">
								<#list screen.getChooseClassType() as options>
									<option id="">${options}</option>
								</#list>
						</select>
						<select id='shortcutFieldName' name='shortcut' onchange="changeFieldContent('shortcut');">
							<#list screen.getChooseFieldName() as options>
								<option id="">${options}</option>
							</#list>
						</select>
						Choose Target <input type="text" id="shortcutTarget" size="5" value=""> 
						<button type="button" onclick="updateTableContent();">Update</button> 
						<br/><br/>
						
						<!--
						Please select if you have multiple values (in single records separated by commas) ? <input type="checkbox" name="multipleValues" id="multipleValues" value="multipleValues">  <br><br/>
						<script>checkMultipleValue('multipleValues', ${screen.getMultipleValue()});</script>
						-->
						<!-- this is the code for uploading the mapping file -->				
						<p> You could upload a mapping file if you have it already </p>
				        <input type="file" name = "uploadMapping"/>
				        <input type="submit" value="upload mapping" onclick="__action.value='uploadMapping';return true;"/>
				        <input type="submit" value="save mapping" onclick="__action.value='saveMapping';return true;"/><br /><br/>						
						
						
						<div id="addScrollBar"  style="overflow:scroll;">
						<table id="table" name="mappingTable" border="1">
							<tr>
								<#list screen.getSpreadSheetHeanders() as header>
									<th>${header}</th>
								</#list>
							</tr>
							<tr>
								<#list screen.getSpreadSheetHeanders() as header>
									<td><div id='${header}_count'></div></td>
									<script type="text/javascript">getCount('${header}_count');</script>
								</#list>
							</tr>
							
							<tr><div id='1'>
								<#list screen.getSpreadSheetHeanders() as header>
									<td><select id='1' name='${header}' onchange="changeFieldContent('${header}');">
										<#list screen.getChooseClassType() as options>
										  <option id="">${options}</option>
										</#list>
										</select>
									</td>
								</#list>
							</div></tr>
							<tr><div id='1'>
								<#list screen.getSpreadSheetHeanders() as header>
									<td><select id='2' name='${header}' onchange="changeFieldContent('${header}');">
										<#list screen.getChooseFieldName() as options>
										  <option id="">${options}</option>
										</#list>
										</select>
									</td>
								</#list>
							</div></tr>
							
							<tr>
								<#list screen.getSpreadSheetHeanders() as header>
									<td><select id='3' name='${header}' onchange="changeFieldContent('${header}');">
										<#list screen.getColumnIndex() as options>
										  <option id="">${options}</option>
										</#list>
										</select>
									</td>
								</#list>
							</tr>
							
							<tr>
								<#list screen.getSpreadSheetHeanders() as header>
									<td>
										Multiple values? <input type="checkbox" name="${header}" id="multipleValues" value="true">
									</td>
								</#list>
							</tr>
							
							<tr>
								<#list screen.getSpreadSheetHeanders() as header>
									<td><div id='${header}'>data type</div>
									</td>
								</#list>
							</tr>
							
							
						</table>
						</div>
						
						<#assign colIndex = 0>
						<#list screen.getMappingForMolgenisEntity() as eachColumn>
							<#assign rowIndex = 0>
							<#assign header = "">
							<#list eachColumn as eachElement>
								<#if (rowIndex > 0)>
									<script>readInMappingFile('${header}' ,'${eachElement}', '${rowIndex}', '${colIndex}');</script>
								<#elseif rowIndex == 0>
									<#assign header = eachElement>
								</#if>
								<#assign rowIndex = rowIndex + 1>
							</#list>
							<#assign colIndex = colIndex + 1>
						</#list>
						
						<h4> ...now you can finish with choosing import: </h4>
				        <input type="submit" value="Next Step" onclick="__action.value='ImportLifelineToPheno';return checkInvestigation();"/>
				        <input type="submit" value="Upload new files" onclick="__action.value='backToPreviousStep';return true;"/><br /><br />			
					<#else>
						</br></br>
						<label> <#if screen.getStatus()?exists>${screen.getStatus()} </#if>  </label>	
					</#if> 
				</div>
			</div>
		</div>
	</div>
	
	
	
</form>
</#macro>

