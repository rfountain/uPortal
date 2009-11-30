<%--

    Copyright (c) 2000-2009, Jasig, Inc.
    See license distributed with this file and available online at
    https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt

--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<portlet:actionURL var="formUrl">
  <portlet:param name="execution" value="${flowExecutionKey}" />
</portlet:actionURL>
        
<!-- Portlet -->
<div class="fl-widget portlet" role="section">
  <!-- Portlet Body -->
  <div class="fl-widget-content portlet-body" role="main">
    <!-- Portlet Section -->
    <div class="portlet-section" role="region">
      <div class="portlet-section-body">   

        <div class="portlet-msg-alert" role="alert">
	        <h3>Warning!</h3>
	        <p>Flushing caches while uPortal is running can slow performance temporarily.
	        Use caution with this feature in production environments.</p>
        </div>

	    <!-- Portlet Section -->
	    <div class="portlet-section" role="region">
	      <div class="portlet-section-body">
            <p>Are you sure you want to flush <c:out value="${cacheName}"/>?</p>
	      </div>
	    </div> <!-- end: portlet-section -->
        
        <form action="${formUrl}" method="POST">
        <div class="portlet-button-group">
	        <input class="portlet-button portlet-button-primary" type="submit" value="Flush cache" name="_eventId_confirm"/>
	        <input class="portlet-button secondary" type="submit" value="Cancel" name="_eventId_cancel"/>
        </div>
        </form>
      </div>
    </div>
  </div>
  
</div>