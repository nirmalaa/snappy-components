<f:view xmlns:f="http://java.sun.com/jsf/core"
        xmlns:h="http://java.sun.com/jsf/html"
        xmlns:ice="http://www.icesoft.com/icefaces/component"
        xmlns:snappy="http://sensemaker.net/snappy"
        
        >
    <html>
    <head>
        <link rel="stylesheet" type="text/css" media="screen, projection" href="snappy.css"/>
    </head>
    <body>
               <ice:form id="form">
<ice:outputText value="#{test.value}"/>      
   <snappy:table list="#{test.list}">
       <snappy:column header="Row" property="row" populate="row"/>
       <snappy:column header="Col A" property="name" populate="form:colA"/>
       <snappy:column header="Col B" property="age" populate="colB"/>
       <snappy:column header="Col C" property="city" populate="colC"/>
   </snappy:table>
     <ice:panelGroup>
         <ice:inputText id="colA" value="#{test.value}"/>
     </ice:panelGroup>

                   </ice:form>
    </body>
    </html>
</f:view>
