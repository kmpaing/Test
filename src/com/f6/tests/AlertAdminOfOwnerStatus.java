/**
 * 
 */
package com.f6.tests;

/**
 * @author kp
 *
 */
import java.util.List;
import java.util.ArrayList;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Identity;
import sailpoint.object.Filter;
import sailpoint.object.QueryOptions;
import sailpoint.object.WorkItem;
import sailpoint.object.ApprovalItem;
import sailpoint.object.ApprovalSet;
import sailpoint.api.Workflower;
import sailpoint.object.Comment;
import sailpoint.object.ManagedAttribute;
import sailpoint.object.EmailOptions;
public class AlertAdminOfOwnerStatus {
	
	 
	SailPointContext ctx = SailPointFactory.getCurrentContext();
	String leaverName = identityName;
	Identity leaverIdentity = ctx.getObjectByName(Identity.class, identityName);
	List allTheThings = new ArrayList();
	 
	//get all the workgroups the leaver is a member of and add to allTheThings
	List wgs = new ArrayList();
	wgs = leaverIdentity.getWorkgroups();
	 
	if (null != wgs){
	Iterator wgIter = wgs.iterator();
	  while (wgIter.hasNext() ){
	  Identity wrkgrp = (Identity)wgIter.next();
	  allTheThings.add("Member of Workgroup: " +wrkgrp.getDisplayName());
	  }
	}
	 
	//get a list of all the catalog items the user is an owner of and add to allTheThings
	int count = 0;
	QueryOptions qo = new QueryOptions();
	Filter filter = Filter.notnull("id");
	qo.addFilter(filter);
	Iterator it = ctx.search(ManagedAttribute.class, qo, "id");
	 
	while(it.hasNext()){
	  count++;
	  String entId = it.next()[0].toString();
	  ManagedAttribute entitlement = (ManagedAttribute) ctx.getObjectById(ManagedAttribute.class, entId);
	  if(entitlement != null){
	  Identity maOwner = entitlement.getOwner();
	  if (maOwner == leaverIdentity){
	System.out.println("Match found on: " + entitlement.getDisplayableName());
	  allTheThings.add("Owner of Entitlement: " + entitlement.getDisplayableName() + " Value: " + entitlement.getValue());
	  }
	  }
	  if(count > 100){
	  ctx.decache();
	   }
	}
	 
	//lets not send the email if allTheThings is empty
	if (allTheThings.isEmpty(){
	return (identityDisplayName + " has nothing important that we need to know about");
	}
	 
	//setting up the email
	EmailOptions options = new EmailOptions();
	EmailTemplate et = ctx.getObjectByName(EmailTemplate.class, "EmailTemplate_YourTemplate");
	options.setSendImmediate(true);
	options.setNoRetry(true);
	options.setVariable("name", leaverName);
	options.setVariable("displayName", identityDisplayName);
	options.setVariable("list", allTheThings);
	ctx.sendEmailNotification(et, options);
}
