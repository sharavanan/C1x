/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.controller;

import com.lrl.c1.bean.AgencyBean;
import com.lrl.c1.bean.MediaplanBean;
import com.lrl.c1.bean.MediaplanlineBean;
import com.lrl.c1.bean.UserprofileBean;
//import com.lrl.c1.buyside.entity.Agency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.lrl.c1.service.MediaPlanService;
import java.util.List;

import com.lrl.c1.entity.Mediaplan;
import com.lrl.c1.entity.Mediaplanline;
import com.lrl.c1.entity.Userprofile;
import com.lrl.c1.service.MediaPlanCreationService;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lrl.c1.service.CommonService;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
/**
 *
 * @author logic
 */
@Controller
public class MediaPlanController {

    //final Logger logger = Logger.getLogger(getClass());
    
    private static final Log log = LogFactory.getLog(MediaPlanController.class);
    
    @Autowired
    private MediaPlanService mediaplanService;
    
    @Autowired
    private MediaPlanCreationService mediaplancreationService;
    
    @Autowired
    private CommonService commonService;
    private static final Logger logger = Logger.getLogger( MediaPlanController.class); 
 

    @RequestMapping("/mediaURL")
    @ResponseBody
    public ModelAndView listMediaPlan() {
        log.debug("Inside the listMediaPlan() methods");
        
        Object[] mediaplan = mediaplanService.FetchAllMediaPlan(1);
        // mediaplan[0] == list //  mediaplan[1] == JSON

        //Map<String, JSONObject> model = new HashMap<String, JSONObject>();
        //model.put("data", (JSONObject) mediaplan[1]);

        Map<String, List> model = new HashMap<String, List>();
        model.put("data", (List) mediaplan[0]);

        return new ModelAndView("mediaPlan", model);
    }

    @RequestMapping("/requestproposals")
    public String listRequestProposal(Model model) {
        log.debug("Inside the  listRequestProposal() methods");
        List<Mediaplan> mediaplan = mediaplanService.FetchReqProposals();
        model.addAttribute("data", mediaplan);
        return "proposals";

    }

    @RequestMapping(value = "/mediacreation", method = RequestMethod.GET)
    public ModelAndView mediaplanCreationPage() {
        log.debug("Inside the  mediaplanCreationPage() methods");
        return new ModelAndView("mediaplanCreation");
    }

    @RequestMapping(value = "/addMedia", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView addMedia() {
        Map<String, Object> model = new HashMap<String, Object>();
        // DON'T DELETE BELOW LINE  
        // mediaplancreationService.createNewMediaPlan( prepareBean(mpBean,mplBean));
        log.debug("Inside the  addMedia() methods");
        return new ModelAndView("mediaplanCreation", model);
    }

    private Mediaplan prepareBean(MediaplanBean mpBean, MediaplanlineBean mplBean) {

        Mediaplan mp = new Mediaplan();
        Mediaplanline mpline = new Mediaplanline();

        // ### hard code for testing purpose 
        AgencyBean agenBean = new AgencyBean();
        agenBean.setId(1);
        UserprofileBean userBean = new UserprofileBean();
        userBean.setId(1);

       // Agency a = new Agency();
       // a.setId(1);
        Userprofile u = new Userprofile();
        u.setId(1);
        // ###


        // set all values to  mediaplan entity

        mp.setName(mpBean.getName());
        mp.setStartDate(mpBean.getStartDate());
        mp.setEndDate(mpBean.getEndDate());
        mp.setIntegrationId(mpBean.getIntegerationId());
        mp.setPublisherList(mpBean.getPublisherList());
        mp.setStatus(mpBean.getStatus());
//        mp.setAgencyId(a);
        mp.setCreatedBy(u);
        mp.setCreated(mpBean.getCreated());
        mp.setUpdatedBy(u);
        mp.setUpdated(mpBean.getUpdated());
        mp.setTotalImpression(mpBean.getTotalImpression());

        // set all values to mediaplanline entity


        mpline.setName(mplBean.getName());
        mpline.setStartDate(mplBean.getStartDate());
        mpline.setEndDate(mplBean.getEndDate());
        mpline.setCpm(mplBean.getCpm());
        mpline.setCurrency(mplBean.getCurrency());
        mpline.setIntegrationId(mplBean.getIntegrationId());
        mpline.setCreatedBy(u);
        mpline.setCreated(mplBean.getCreated());
        mpline.setUpdatedBy(u);
        mpline.setUpdated(mplBean.getUpdated());
        mpline.setStatus(mplBean.getStatus());
        mpline.setCurrency(mplBean.getCurrency());
        mpline.setInventoryApproved(mplBean.getInventoryApproved());
        mpline.setInventoryProposed(mplBean.getInventoryProposed());

        mpline.setPlanId(mp);
        List<Mediaplanline> ml = new ArrayList<Mediaplanline>();
        ml.add(mpline);
        mp.setMediaplanlineList(ml);

        return mp;

    }

 @RequestMapping(value = "/lineList", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView lineList() {
        Map<String, Object> model = new HashMap<String, Object>();        
        log.debug("Inside the  lineList() methods");
        return new ModelAndView("mediacreation", model);
    }
 
// @RequestMapping("/categorylistmap")
//    public String   getCategoryListAsMap(Model model) {      
//       HashMap m  = (HashMap) commonService.getCategoryListAsMap();
//       model.addAttribute("data",m);
//       return "category";    
//  } 
//   
//    @RequestMapping("/categorylistjson")
//    public String  getCategoryListAsJson(Model model) {      
//       String s = commonService.getCategoryListAsJson();               
//       model.addAttribute("data",s);
//       return "category";    
//  }  
//    
//  
//     @RequestMapping("/placementlistjson")
//    public String  getPlacementListAsJson(Model model) {      
//       String s = commonService.getPlacementListAsJson();               
//       model.addAttribute("data",s);
//       return "category";    
//  }  
//     
//     @RequestMapping("/placementlistmap")
//    public String  getPlacementListAsMap(Model model) {      
//       Map m = commonService.getPlacementListAsMap();              
//       model.addAttribute("data",m);
//       return "category";    
//  }  
    
   @RequestMapping("/publisheroverviewname")  
  public String getPublisherOverviewByName(Model model){
         String overview  = new String();
         String publisher = "yatra.com";         
         overview = commonService.getPublisherOverviewByName(publisher);                   
         model.addAttribute("data",overview);
         return "category";
    }
   @RequestMapping("/publisheroverviewid")  
   public String getPublisherOverviewById(Model model){
         String overview  = new String();
         int id = 345; // for egm car tech
         overview = commonService.getPublisherOverviewById(id);
         model.addAttribute("data",overview);      
         return "category";
     }   
  
   @RequestMapping("/publishercontactmap")  
   public String getPublisherContactAsMap(Model model){
        int publisher_id = 567;        
        HashMap m  = (HashMap) commonService.getPublisherContactAsMap(publisher_id);
        model.addAttribute("data",m);
        return "category";            
     }  
        
    /*List of values sri */
   @RequestMapping("/gender")  
   public String getGender(Model model){   
        String s  =  commonService.getGender();
        model.addAttribute("data",s);
        return "category";            
     }  
    
   @RequestMapping("/agerange")  
   public String getAgeRange(Model model){           
        String s  =  commonService.getAgeRange();
        model.addAttribute("data",s);
        return "category";            
     }  
    
   @RequestMapping("/placementtypes")  
   public String getPlacementTypes(Model model){           
        String s  =  commonService.getPlacementTypes();
        model.addAttribute("data",s);
        return "category";            
     }  
    
   @RequestMapping("/categorylist")  
   public String getCategoryList(Model model){           
        List s  =  commonService.getCategoryList();
        model.addAttribute("data",s);
        return "category";            
     }  
    
    
   @RequestMapping("/countries")  
   public String getCountries(Model model){           
        String s  =  commonService.getCountries();
        model.addAttribute("data",s);
        return "category";            
     }      
     
   @RequestMapping("/states")  
   public String getStates(Model model){     
        String c = "USA"; 
        String s  =  commonService.getStates(c);
        model.addAttribute("data",s);
        return "category";            
     }  
    
     
   @RequestMapping("/cities")  
   public String getCities(Model model){     
        String c  = "California"; 
        String s  =  commonService.getCities(c);
        model.addAttribute("data",s);
        return "category";            
     }   


}
