var NAVTREE =
[
  [ "Functional Mockup Interface For EnergyPlus", "index.html", [
    [ "Class List", "annotated.html", [
      [ "Element", "structElement.html", null ],
      [ "fmiCallbackFunctions", "structfmiCallbackFunctions.html", null ],
      [ "fmiEventInfo", "structfmiEventInfo.html", null ],
      [ "FMU", "structFMU.html", null ],
      [ "ListElement", "structListElement.html", null ],
      [ "ModelDescription", "structModelDescription.html", null ],
      [ "ModelInstance", "structModelInstance.html", null ],
      [ "ScalarVariable", "structScalarVariable.html", null ],
      [ "Stack", "structStack.html", null ],
      [ "Type", "structType.html", null ],
      [ "XML_cp", "structXML__cp.html", null ],
      [ "XML_Encoding", "structXML__Encoding.html", null ],
      [ "XML_Expat_Version", "structXML__Expat__Version.html", null ],
      [ "XML_Feature", "structXML__Feature.html", null ],
      [ "XML_Memory_Handling_Suite", "structXML__Memory__Handling__Suite.html", null ],
      [ "XML_ParsingStatus", "structXML__ParsingStatus.html", null ]
    ] ],
    [ "Class Index", "classes.html", null ],
    [ "Class Members", "functions.html", null ],
    [ "File List", "files.html", [
      [ "expat.h", "expat_8h.html", null ],
      [ "expat_external.h", "expat__external_8h.html", null ],
      [ "fmiFunctions.h", "fmiFunctions_8h.html", null ],
      [ "fmiModelFunctions.h", "fmiModelFunctions_8h.html", null ],
      [ "common/fmiModelTypes.h", "common_2fmiModelTypes_8h.html", null ],
      [ "include/fmiModelTypes.h", "include_2fmiModelTypes_8h.html", null ],
      [ "fmiPlatformTypes.h", "fmiPlatformTypes_8h.html", null ],
      [ "fmuTemplate.c", "fmuTemplate_8c.html", null ],
      [ "fmuTemplate.h", "fmuTemplate_8h.html", null ],
      [ "main.c", "main_8c.html", null ],
      [ "main.h", "main_8h.html", null ],
      [ "parser.c", "parser_8c.html", null ],
      [ "parser.h", "parser_8h.html", null ],
      [ "stack.c", "stack_8c.html", null ],
      [ "stack.h", "stack_8h.html", null ],
      [ "util.c", "util_8c.html", null ],
      [ "util.h", "util_8h.html", null ],
      [ "xml_parser_cosim.c", "xml__parser__cosim_8c.html", null ],
      [ "xml_parser_cosim.h", "xml__parser__cosim_8h.html", null ]
    ] ],
    [ "Directories", "dirs.html", [
      [ "src", "dir_fa76d6f64327e3e6f9986cc67ccae5d8.html", [
        [ "common", "dir_170a395f73a910f84da2c658fae1d224.html", [
          [ "expat.h", "expat_8h.html", null ],
          [ "expat_external.h", "expat__external_8h.html", null ],
          [ "fmiFunctions.h", "fmiFunctions_8h.html", null ],
          [ "common/fmiModelTypes.h", "common_2fmiModelTypes_8h.html", null ],
          [ "fmiPlatformTypes.h", "fmiPlatformTypes_8h.html", null ],
          [ "stack.c", "stack_8c.html", null ],
          [ "stack.h", "stack_8h.html", null ],
          [ "util.c", "util_8c.html", null ],
          [ "util.h", "util_8h.html", null ],
          [ "xml_parser_cosim.c", "xml__parser__cosim_8c.html", null ],
          [ "xml_parser_cosim.h", "xml__parser__cosim_8h.html", null ]
        ] ],
        [ "demo-win", "dir_7bae4761f8506d4f69613b3b0d0415ab.html", [
          [ "main.c", "main_8c.html", null ],
          [ "main.h", "main_8h.html", null ]
        ] ],
        [ "include", "dir_7e7217abfca8ab3922ed36bdd490061f.html", [
          [ "fmiModelFunctions.h", "fmiModelFunctions_8h.html", null ],
          [ "include/fmiModelTypes.h", "include_2fmiModelTypes_8h.html", null ],
          [ "fmuTemplate.c", "fmuTemplate_8c.html", null ],
          [ "fmuTemplate.h", "fmuTemplate_8h.html", null ]
        ] ],
        [ "parser", "dir_1f7e472cebed5d23ee09693f3bdd1e0e.html", [
          [ "parser.c", "parser_8c.html", null ],
          [ "parser.h", "parser_8h.html", null ]
        ] ]
      ] ]
    ] ],
    [ "File Members", "globals.html", null ]
  ] ]
];

function createIndent(o,domNode,node,level)
{
  if (node.parentNode && node.parentNode.parentNode)
  {
    createIndent(o,domNode,node.parentNode,level+1);
  }
  var imgNode = document.createElement("img");
  if (level==0 && node.childrenData)
  {
    node.plus_img = imgNode;
    node.expandToggle = document.createElement("a");
    node.expandToggle.href = "javascript:void(0)";
    node.expandToggle.onclick = function() 
    {
      if (node.expanded) 
      {
        $(node.getChildrenUL()).slideUp("fast");
        if (node.isLast)
        {
          node.plus_img.src = node.relpath+"ftv2plastnode.png";
        }
        else
        {
          node.plus_img.src = node.relpath+"ftv2pnode.png";
        }
        node.expanded = false;
      } 
      else 
      {
        expandNode(o, node, false);
      }
    }
    node.expandToggle.appendChild(imgNode);
    domNode.appendChild(node.expandToggle);
  }
  else
  {
    domNode.appendChild(imgNode);
  }
  if (level==0)
  {
    if (node.isLast)
    {
      if (node.childrenData)
      {
        imgNode.src = node.relpath+"ftv2plastnode.png";
      }
      else
      {
        imgNode.src = node.relpath+"ftv2lastnode.png";
        domNode.appendChild(imgNode);
      }
    }
    else
    {
      if (node.childrenData)
      {
        imgNode.src = node.relpath+"ftv2pnode.png";
      }
      else
      {
        imgNode.src = node.relpath+"ftv2node.png";
        domNode.appendChild(imgNode);
      }
    }
  }
  else
  {
    if (node.isLast)
    {
      imgNode.src = node.relpath+"ftv2blank.png";
    }
    else
    {
      imgNode.src = node.relpath+"ftv2vertline.png";
    }
  }
  imgNode.border = "0";
}

function newNode(o, po, text, link, childrenData, lastNode)
{
  var node = new Object();
  node.children = Array();
  node.childrenData = childrenData;
  node.depth = po.depth + 1;
  node.relpath = po.relpath;
  node.isLast = lastNode;

  node.li = document.createElement("li");
  po.getChildrenUL().appendChild(node.li);
  node.parentNode = po;

  node.itemDiv = document.createElement("div");
  node.itemDiv.className = "item";

  node.labelSpan = document.createElement("span");
  node.labelSpan.className = "label";

  createIndent(o,node.itemDiv,node,0);
  node.itemDiv.appendChild(node.labelSpan);
  node.li.appendChild(node.itemDiv);

  var a = document.createElement("a");
  node.labelSpan.appendChild(a);
  node.label = document.createTextNode(text);
  a.appendChild(node.label);
  if (link) 
  {
    a.href = node.relpath+link;
  } 
  else 
  {
    if (childrenData != null) 
    {
      a.className = "nolink";
      a.href = "javascript:void(0)";
      a.onclick = node.expandToggle.onclick;
      node.expanded = false;
    }
  }

  node.childrenUL = null;
  node.getChildrenUL = function() 
  {
    if (!node.childrenUL) 
    {
      node.childrenUL = document.createElement("ul");
      node.childrenUL.className = "children_ul";
      node.childrenUL.style.display = "none";
      node.li.appendChild(node.childrenUL);
    }
    return node.childrenUL;
  };

  return node;
}

function showRoot()
{
  var headerHeight = $("#top").height();
  var footerHeight = $("#nav-path").height();
  var windowHeight = $(window).height() - headerHeight - footerHeight;
  navtree.scrollTo('#selected',0,{offset:-windowHeight/2});
}

function expandNode(o, node, imm)
{
  if (node.childrenData && !node.expanded) 
  {
    if (!node.childrenVisited) 
    {
      getNode(o, node);
    }
    if (imm)
    {
      $(node.getChildrenUL()).show();
    } 
    else 
    {
      $(node.getChildrenUL()).slideDown("fast",showRoot);
    }
    if (node.isLast)
    {
      node.plus_img.src = node.relpath+"ftv2mlastnode.png";
    }
    else
    {
      node.plus_img.src = node.relpath+"ftv2mnode.png";
    }
    node.expanded = true;
  }
}

function getNode(o, po)
{
  po.childrenVisited = true;
  var l = po.childrenData.length-1;
  for (var i in po.childrenData) 
  {
    var nodeData = po.childrenData[i];
    po.children[i] = newNode(o, po, nodeData[0], nodeData[1], nodeData[2],
        i==l);
  }
}

function findNavTreePage(url, data)
{
  var nodes = data;
  var result = null;
  for (var i in nodes) 
  {
    var d = nodes[i];
    if (d[1] == url) 
    {
      return new Array(i);
    }
    else if (d[2] != null) // array of children
    {
      result = findNavTreePage(url, d[2]);
      if (result != null) 
      {
        return (new Array(i).concat(result));
      }
    }
  }
  return null;
}

function initNavTree(toroot,relpath)
{
  var o = new Object();
  o.toroot = toroot;
  o.node = new Object();
  o.node.li = document.getElementById("nav-tree-contents");
  o.node.childrenData = NAVTREE;
  o.node.children = new Array();
  o.node.childrenUL = document.createElement("ul");
  o.node.getChildrenUL = function() { return o.node.childrenUL; };
  o.node.li.appendChild(o.node.childrenUL);
  o.node.depth = 0;
  o.node.relpath = relpath;

  getNode(o, o.node);

  o.breadcrumbs = findNavTreePage(toroot, NAVTREE);
  if (o.breadcrumbs == null)
  {
    o.breadcrumbs = findNavTreePage("index.html",NAVTREE);
  }
  if (o.breadcrumbs != null && o.breadcrumbs.length>0)
  {
    var p = o.node;
    for (var i in o.breadcrumbs) 
    {
      var j = o.breadcrumbs[i];
      p = p.children[j];
      expandNode(o,p,true);
    }
    p.itemDiv.className = p.itemDiv.className + " selected";
    p.itemDiv.id = "selected";
    $(window).load(showRoot);
  }
}

