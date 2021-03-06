<%-- 
    Document   : mediaplanCreation
    Created on : Nov 15, 2013, 6:03:52 PM
    Author     : logic
--%>

 <%@page contentType="text/html" pageEncoding="UTF-8"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

 <!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>C1X- Media Plans</title>

<script type="text/javascript" src="../table.js"></script>
<link rel="stylesheet" type="text/css" href="../table.css" media="all">
<script language='JavaScript'>
 
checked = false;
 
function checkedAll () {
 
if (checked == false){checked = true}else{checked = false}
 
for (var i = 0; i < document.getElementById('mediaform').elements.length; i++) {
 
document.getElementById('mediaform').elements[i].checked = checked;
 
}
 return true;
}
 
</script>
<style type="text/css">
body
	{
	margin: 0; /* margin and padding only necessary to cater for Mac IE5 */
	padding: 0;
	/*\*/	overflow: hidden; /* because Mac IE5 don't understand */
	}
div
	{
	margin: 0;
	padding: 0;
	}
#content
	{
	
	 
	position: absolute;
	top: 11%;
	/*\*//*right: 31%;*/
	left: 21%;   /* Exclude right and left props for Mac IE5 */
	/*margin: 0 31% 0 21%;*/
	width:79%;
	/*\*/ margin: 0; /* */
	/*height: 59%;*/
	bottom: 5%;
	overflow:auto; 
	
	}
#sidebar-left
	{
	position: absolute;
	top: 10%;
	bottom: 0;
	left: 0;
	width: 20%;
	/*overflow: auto;*/
	}
#sidebar-right
	{
	position: absolute;
	top:  0;
	right: 0;
	bottom: 0;
	overflow: auto;
	width: 30%;
	}
#header
	{
	position: absolute;
	top: 0;
	/*\*//*right: 31%;*/
	left: 0%;   /* Exclude right and left props for Mac IE5 */
	/*margin: 0 31% 0 21%;*/
	width:100%;
	/*\*/ margin: 0; /* */
	height: 10%;
	}
#footer
	{
	/*\*/ position: absolute;  /* Exclude for Mac IE5 */
	/*right: 31%;*/
	bottom: 0;
	left: 0;
	height: 5%;
	width:100%;
	margin: 0 31% 0 21%; /* Cater for Mac IE5 */
	/*\*/ margin: 0; /* Put back for all the rest */
	}
#footer h4
	{
	position: absolute;
	bottom: 0;
	width: 100%;
	margin: 0;
	padding: 0;
	}

</style>
<!--[if lt IE 7]>
<style type="text/css" media="screen">
/* let the IE expressions commence */
#sidebar-left
	{
	height: expression(document.body.clientHeight + "px");
	}
#sidebar-right
	{
	height: expression(document.body.clientHeight + "px");
	}
#content
	{
	height: expression(document.body.clientHeight - ( (document.body.clientHeight * 15 / 100) + (document.body.clientHeight * 25 / 100) + (document.body.clientHeight * 1 / 100 * 2) ) + "px");
	width: expression(document.body.clientWidth - ( (document.body.clientWidth * 20 / 100) + (document.body.clientWidth * 30 / 100) + (document.body.clientWidth * 1 / 100 * 2) ) + "px");
	}
#header
	{
	width: expression(document.body.clientWidth - ( (document.body.clientWidth * 20 / 100) + (document.body.clientWidth * 30 / 100) + (document.body.clientWidth * 1 / 100 * 2) ) + "px");
	}
#footer
	{
	width: expression(document.body.clientWidth - ( (document.body.clientWidth * 20 / 100) + (document.body.clientWidth * 30 / 100) + (document.body.clientWidth * 1 / 100 * 2) ) + "px");
	}
</style>
<![endif]-->
<style type="text/css">
/* settings for appearance alone */
body { background: #e6e7e9; font-family:"Lucida Grande", "Lucida Sans Unicode", "Lucida Sans", "DejaVu Sans", Verdana, sans-serif; }
#content { /*background: #cccccc;*/ }
#sidebar-left { background: #5a6775;border-top:#e6e7e9 solid 1px; }
#sidebar-right { background: #ffcc99; }
#header { background: #3c4c5f; }
#footer { background: #3c4c5f; }
#footer h4 { background: #ccffcc; }
/* convenience div for padding inside positioned divs */
div.paddingwrap
	{
	margin: 0;
	/*padding: 1em;*/
	}
p, h1, h2, h4, li
	{
	margin: 0;
	padding: 0;
	}
h3
	{
	margin: 1em 0 0 0;
	}
ul
	{
	margin: 0 0 0em 0em;
	padding: 0;
	}
code
	{
	display: block;
	background-color: #dddddd;
	}
 #header h1{
  margin:1% 0 0 5%;
  color:#FFF;
 }
li{
	padding:10px 0px 10px 10px;		
	margin-left:0px;
	list-style:none;
	color:#e6e7e9;
	font-size:12px;
 }
 li a{
	color:#e6e7e9;
	text-decoration:none; 
	 }
	 li input{
	width:80%;	 
	color:#3c4c5f;	 
	border-radius:3px;
	border:none;
	height:20px;
	background-color:#d1d2d4;
		 }
.paddingwrap .search,.newmedia,.delete{
	float:left;
	padding:20px;
	}
	
	.search input{
		width:250px;
		color:#3c4c5f;	 
	border-radius:3px;
	border:none;
	height:20px;
	 
		}
	.newmedia button{
		 
		color:#ffffff;	 
	border-radius:3px;
	border:none;
	height:20px;
	 background-color:#2cbdbc;
		}	
		.delete button,.export button{
		 
		color:#ffffff;	 
	border-radius:3px;
	border:none;
	height:20px;
	background-color:#d1d2d4;
		}	
		div .export{
	 
	 float:right;padding:20px;
			}
			div .data,table{
	width:99%	;
	float:left;
				}
table th {
    background-color:#333333;
	color:#CCC;
	font-size:10px; 
	font-weight:100;
}

table tr  { 
	
	font-size:10px; 
	height:25px;
}
  table td:nth-child(2) {  color:#126BAF;font-weight:bold;}
	.current{
	background-color:#3c4c5f; 
	/* background-image:url("sortable.gif");
	background-position:center right;background-repeat:no-repeat;*/
		}
.arrow-left {
	width: 0; 
	height: 0; 
	border-top: 10px solid transparent;
	border-bottom: 10px solid transparent; 
	float:right;
	margin-right:0px;
	border-right:10px solid white; 
}
.arrow-down {
	width: 0; 
	height: 0; 
	border-left: 5px solid transparent;
	border-right: 5px solid transparent; 
	float:right;
	margin-left:5px;
	margin-top:3px;
	border-top:5px solid white; 
}

.breadcrumb{
	position:absolute;
background-color:#CCC;
color:#18325D;
width:80%;
left:20%;
/*float:right;*/
height:30px;	
bottom:0px;
padding:5px 10px 0px 20px;
	}
.chkbox{
 background-color:#333333;padding: 0px 0px 0px 0px;	
	}
</style>
</head>
<body>
<div id="content">
<div class="paddingwrap">
 
 <div class="search">
     <input type="text" name="searchMP" id="searchMP" value="Type to search for Media Plans"  />
 </div>
 <div  class="newmedia">
     <button type="button" name="newMP" id="newMP" > + New media Plan</button> 
 </div>
<div class="delete"> 
    <button type="button" name="deleteMP" id="deleteMP" > Delete</button> 
</div>
<div  class="export"> <button type="button" name="expMP" id="expMP" > Export CSV</button> </div>
<div class="data">
 <form:form name="mediaform" id="mediaForm" action="" >
<table align="center" class="example table-autosort:0 <!--table-stripeclass:alternate-->" id="TABLE_2" rules="rows"><thead>
<tr><td  class="chkbox" style="background-color:#333333;padding:0px 0px 0px 0px;"><input type="checkbox" id='checkall' name='checkall' onclick='checkedAll();'/></td><th   nowrap="nowrap" align="left" class="table-sortable:default table-sortable">Media Plan Name</th>
<th align="left" class="table-sortable:date table-sortable">Start Date </th>
<th align="left" class="table-sortable:date table-sortable">End Date</th>
<th align="left" class="table-sortable:numeric table-sortable">Placements Selected</th>
<th align="left" class="table-sortable:numeric table-sortable">Estimated Impressions</th> 
<th align="left" class="table-sortable:default table-sortable">Creatives Uploaded</th>
<th align="left" class="table-sortable:default table-sortable table-sorted-desc">publisher List</th>
</tr></thead>
<tbody>
<tr>
    <td style="padding:0px 0px 0px 0px;"><input type="checkbox" /> </td>
    <td>20s Female Plan</td>
    <td nowrap="nowrap">December 1st, 2014</td>
    <td nowrap="nowrap">December 31st, 2014</td>
    <td align="center">5</td>
    <td align="center">2,000,000</td>
    <td align="center">1 of 3</td>
    <td nowrap="nowrap">yahoo.com, people.com, vogue.com, ...</td>
</tr>
<tr>
    <td style=" padding: 0px 0px 0px 0px;"><input type="checkbox" id="1" name="1"/></td><td>Summer Fashion Plan</td>
    <td>December 1st, 2014</td><td>December 31st, 2014</td>
    <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
    <td>yahoo.com, people.com, vogue.com, ...</td>
</tr>
<tr>
    <td style=" padding: 0px 0px 0px 0px;"><input type="checkbox" id="2" name="2"/></td>
    <td>Mobile Run-of Plan</td>
    <td>December 1st, 2014</td><td>December 31st, 2014</td>
    <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
    <td>yahoo.com, people.com, vogue.com, ...</td>
</tr>
<tr>
    <td style=" padding: 0px 0px 0px 0px;"><input type="checkbox" id="3" name="3"/></td><td>Brand Video Plan</td>
    <td>December 1st, 2014</td>
    <td>December 31st, 2014</td>
    <td>&nbsp;</td><td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>yahoo.com, people.com, vogue.com, ...</td>
</tr>
<tr>
    <td style="padding: 0px 0px 0px 0px;"><input type="checkbox" id="4" name="4"/></td><td>Hot Indian Summer Plan</td>
    <td>December 1st, 2014</td>
    <td>December 31st, 2014</td>
    <td>&nbsp;</td><td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>yahoo.com, people.com, vogue.com, ...</td>
</tr>
</tbody>
</table></form:form></div>
<!--<h2>END CONTENT</h2>-->
</div><!-- close div.paddingwrap -->
</div><!-- close div#content -->
<div id="sidebar-left">
<div class="paddingwrap">
<ul>
    <li>
        <input type="text" name="5" id="5" value="Type Keyword to search" />
    </li>
    <li>Overview</li>
    <li>        
        <ul><c:url var="mediaLink" value="/mediaURL" />
            
            <li class="current"><a href="${mediaLink}">Media Plans</a><span class="arrow-left"></span></li>
            <c:url var="addmedia" value="/addMedia" />
            <li><a  href="${addmedia}">Request For Proposals</a></li>
               <li><a  href="../proposals.html">Request For Proposals</a></li>
            <li><a href="#">Insertion Orders</a></li>
             <li><a href="#">Contracts</a></li>
             <li><a href="#">Campaigns</a></li>

        </ul>
    </li>
</ul>
</div>
</div>

<div id="header">
    <h1>C1X</h1>
    <span style="float:right;position:absolute;top:5px; right:150px;color:white;font-size:10px;">danku@c1x.com<span class="arrow-down"></span></span>
    <div class="breadcrumb">Media Plans</div>
</div><!-- close div#header -->
<div id="footer"></div>


</body></html>
