/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.service;

import com.lrl.c1.bean.MediaplanBean;
import com.lrl.c1.bean.TargetingBean;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lrl.c1.dao.MediaPlanDAO;
import com.lrl.c1.entity.Invtargeting;
import com.lrl.c1.entity.Mediaplan;
import com.lrl.c1.entity.Mediaplanline;
import com.lrl.c1.entity.Targeting;
import com.lrl.c1.wrapper.MediaplanData;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.ListIterator;
import org.json.JSONObject;

/**
 *
 * @author logic
 */
@Service("mediaplanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MediaPlanServiceImpl implements MediaPlanService {

    @Autowired
    private MediaPlanDAO mediaplanDao;
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
//    @Autowired
//    private MediaplanBean mpBean;
    
    //public List<Mediaplan> FetchAllMediaPlan(int userid){    
    public Object[] FetchAllMediaPlan(int userid) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Mediaplan> FetchAllMediaPlanlist = mediaplanDao.FetchAllMediaPlan(userid);
        // for our reference ###
        System.out.println("Name           | Targeting | Start Dt | End Dt | Inventory | Status | Price  | Action ");
        System.out.println("======================================================================================");
        // ###
        ListIterator mplist = FetchAllMediaPlanlist.listIterator();
        Object[] o = new Object[2];

        JSONObject json = new JSONObject();

        List alst = new ArrayList();

        while (mplist.hasNext()) {   // MEDIA PLAN LIST

            Mediaplan r = (Mediaplan) mplist.next();
            List<Mediaplanline> lst = r.getMediaplanlineList();
            ListIterator mpl = lst.listIterator();
            int target = 0;
            int publisher = 0;
            int adunits = 0;
            BigDecimal CPM = new BigDecimal(0);

            while (mpl.hasNext()) {
                Mediaplanline mpll = (Mediaplanline) mpl.next();
                target += mpll.getTargetingList().size(); // point to Targeting table 
                adunits += mpll.getInvtargetingList().size();

                CPM = CPM.add(mpll.getCpm());
                publisher += mpll.getPublisherId().getMediaplanlineList().size();

            }

            double price = 0;

            if (!lst.isEmpty()) {
                price = CPM.doubleValue() / lst.size();
            }

            // ## for test
            System.out.println(r.getName() + "  | " + target + " | " + sdf.format(r.getStartDate()) + " | " + sdf.format(r.getEndDate()) + " | [ " + this.getPublisherCount(r.getId()).getPubCount() + " - " + lst.size() + " - " + adunits + " ] | " + r.getStatus() + " | " + price + " | Delete");
            System.out.println("____________________________________________________________________________________________");
            // ##     
            
            List l = new ArrayList();


            // SECOND ROW iteration 
            //Object[] seclayer = this.secondLayer(r.getId());
          
            l.add(r.getName());
            l.add(target);
            l.add(sdf.format(r.getStartDate()));
            l.add(sdf.format(r.getEndDate()));
            l.add(r.getTotalImpression());
            List inv = new ArrayList();
                    inv.add(this.getPublisherCount(r.getId()).getPubCount());
                    inv.add(lst.size());
                    inv.add(adunits);
            l.add(inv);
            l.add(r.getStatus());
            l.add(price);
         //   l.add(seclayer[0]);

            JSONObject beanJSON = new JSONObject();
            JSONObject beanTargetJSON = new JSONObject();

            beanJSON.put("MediaPlanId", r.getId());
            beanJSON.put("MediaPlan", r.getName());
            beanJSON.put("TargetingGroups", target);
            beanJSON.put("StartDate", sdf.format(r.getStartDate()));
            beanJSON.put("EndDate", sdf.format(r.getEndDate()));
            beanJSON.put("Impression", r.getTotalImpression());
            beanTargetJSON.put("Publishers", this.getPublisherCount(r.getId()).getPubCount());
            beanTargetJSON.put("Placement", lst.size());
            beanTargetJSON.put("AdUnits", adunits);
            beanJSON.put("Inventory", beanTargetJSON);
            beanJSON.put("Status", r.getStatus());
            beanJSON.put("Price", price);
         //   beanJSON.put("mediaplanloop", seclayer[1]);

            alst.add(l);
            json.append("MEDIA", beanJSON);



        }  // END MEDIA PLAN LIST 
        System.out.println("****************************************************************************");
        o[0] = alst;
        o[1] = json;
        //o[1]=json.toString(5); // for pretty print
        // return mediaplanDao.FetchAllMediaPlan(userid);
        return o;
    }
   
    public List<Mediaplan> FetchMediaPlan(int userid){            
        return mediaplanDao.FetchAllMediaPlan(userid);
    }    
    
    public MediaplanBean getPublisherCount(Integer mpid) {
        return mediaplanDao.getPublisherCount(mpid);
    }

    public List<Mediaplan> FetchReqProposals() {
        return mediaplanDao.FetchReqProposals();
    }

    public List secondLayer(Integer mpid) {
        return mediaplanDao.secondLayer(mpid);
    }
    
    public List thirdLayer(Integer mid, Integer pid){
        return   mediaplanDao.thirdLayer(mid,pid);
        
    }
    
    public org.codehaus.jettison.json.JSONObject fourthLayer(Integer mplid, Integer pid){
        return mediaplanDao.fourthLayer(mplid, pid);
    }
    
    public Object[] fifthLayer(Integer mplid, Integer aduid, Integer pid){
        return mediaplanDao.fifthLayer(mplid, aduid, pid);
    }
    
    
    
    // ### 
    
    public List<MediaplanData> getMediaPlanGrpList(String mpname,String adname,String status){
        
        List<Mediaplan> list = mediaplanDao.getMediaPlanList(mpname, adname, status);
         
        List<MediaplanData> lst = new ArrayList<MediaplanData>();

        for (Mediaplan mp : list) {
            Integer mpid = mp.getId();
            String name = mp.getName();
            String sdt = sdf.format(mp.getStartDate());
            String edt = sdf.format(mp.getEndDate());
            Integer impression = mp.getTotalImpression();            
            String Status = mp.getStatus();
            Integer targeting =mediaplanDao.getMediaPlanTargetingCount(mpid);
            Integer invPublishers =mediaplanDao.getMediaPlanPublishersCount(mpid);
            Integer invPlacements =mediaplanDao.getMediaPlanPlacementsCount(mpid);
            Integer invAdunits =mediaplanDao.getMediaPlanAdunitsCount(mpid); 
            Double CPM = mediaplanDao.getMediaPlanCPM(mpid);
            MediaplanData mediaplanData = new MediaplanData(mpid, name, targeting, sdt, edt, invPublishers, invAdunits, invPlacements, impression, CPM, Status);
            mediaplanData.setAdvertiserid(mp.getAdvertiserId().getId());
            mediaplanData.setAdvertisername(mp.getAdvertiserId().getName());
            lst.add(mediaplanData);
            
            
        }
        return   lst;
        
    }
    
        // ###
    public List<MediaplanData> getMediaPlanPublishersList(Integer mpid){
        
        List<Mediaplanline> list = mediaplanDao.getMediaPlanPublisherList(mpid);
                      
        List<MediaplanData> lst = new ArrayList<MediaplanData>();
        
        for (Mediaplanline mpl : list) {
                    
                Integer pubid = mpl.getPublisherId().getId();
                String name = mpl.getPublisherId().getName();                 
                String sdt = sdf.format(mediaplanDao.getMinStartdt(mpid, pubid));
                String edt = sdf.format(mediaplanDao.getMaxEnddt(mpid, pubid));
                Integer impression = mediaplanDao.getPublisherImpressions(mpid, pubid);          
                String Status = mpl.getStatus(); //mediaplanDao.getPublisherStatus(mpid, pubid);
                Integer targeting = mediaplanDao.getPublisherTargetingCount(mpid, pubid);
                Integer invPublishers =null;
                Integer invPlacements =mediaplanDao.getPublisherPlacementCount(  mpid,   pubid);
                Integer invAdunits =mediaplanDao.getPublisherAdunitCount(mpid,pubid);
                Double CPM = mediaplanDao.getPublisherCPM(mpid, pubid);
                MediaplanData mediaplanData = new MediaplanData(pubid, name, targeting, sdt, edt, invPublishers, invAdunits, invPlacements, impression, CPM, Status);
                lst.add(mediaplanData);
        }
        return   lst;
    }
    
    
    
         // ###
    public List<MediaplanData> getMediaPlanPlcList(Integer mpid, Integer pubid){
        
        List<Mediaplanline> list = mediaplanDao.getMediaPlanPlacementList(mpid, pubid);
                      
        List<MediaplanData> lst = new ArrayList<MediaplanData>();
        
        for (Mediaplanline mpl : list) {
                    
                Integer mplid = mpl.getId();
                String name = mpl.getName();                                 
                List<Targeting> tar =mediaplanDao.getPlacementTargetingCount(mplid); // doubt              
                List<TargetingBean> tb = new ArrayList<TargetingBean>();                 
                for(Targeting t : tar){
                    TargetingBean data = new TargetingBean(t.getId(), t.getName(),t.getValue());
                    tb.add(data);
                }
                
                
                Integer invPublishers =null;
                Integer invPlacements =null;
                Integer invAdunits = mediaplanDao.getPlacementAdUnitsCount(mplid, pubid);
                Double CPM = Double.valueOf(mpl.getCpm().toString());
                Integer impression = mpl.getInventoryApproved();
                String Status = mpl.getStatus();
                String sdt = sdf.format(mpl.getStartDate());
                String edt = sdf.format(mpl.getEndDate());
                
                //MediaplanData mediaplanData = new MediaplanData(pubid, name, tb, sdt, edt, invPublishers, invAdunits, invPlacements, impression, CPM, Status); // old 
                MediaplanData mediaplanData = new MediaplanData(pubid, name, tb, sdt, edt, invPublishers, invAdunits, invPlacements, impression, CPM, Status, mplid); // sharan
                lst.add(mediaplanData);
        }
        return   lst;
    }
    
       
    // ###
    
    public List<MediaplanData> getMediaPlanAdunitList(Integer mplid, Integer pubid){
        
        List<Invtargeting> adulist = mediaplanDao.getMediaPlanAdUnitList(mplid, pubid);
        
        List<Invtargeting> plclist = mediaplanDao.getMediaPlanAdunitsPlacementList(mplid, pubid);
                      
        List<MediaplanData> lst = new ArrayList<MediaplanData>();
        System.out.println("placement list ==   "+plclist.size());
        System.out.println("adu list ==   "+adulist.size());
        
        
        List<Invtargeting> list = new ArrayList<Invtargeting>() ;
        if(adulist.size()>0)  list.addAll(adulist);
        if(plclist.size()>0) list.addAll(plclist);
        
        System.out.println(" list is ==   "+list);
        try{
        for (Invtargeting inv : list) {
                    
                Integer aduid = inv.getAdUnitId().getId();
                String name = (inv.getPlacementId().getId()==0)?inv.getAdUnitId().getName():inv.getPlacementId().getName();                      
                String sdt = null;
                String edt = null;
                Integer targeting=null;
                Integer invPublishers =null;
                Integer invPlacements =null;                
                Integer invSubAdunits = mediaplanDao.getInvTarAdunitsCount(mplid, pubid, aduid);
                Double CPM = Double.valueOf(inv.getCpm().toString());                
                Integer impression = mediaplanDao.getInvTarAdunitsImpression(mplid, pubid, aduid);                
                String Status = inv.getStatus();                               
                MediaplanData mediaplanData = new MediaplanData(aduid, name, targeting, sdt, edt, invPublishers, invSubAdunits, invPlacements, impression, CPM, Status);
                mediaplanData.setAdunitId(inv.getAdUnitId().getId());
                mediaplanData.setPlacementId(inv.getPlacementId().getId());
                
                
                lst.add(mediaplanData);
        }
        
        
        
        return   lst;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }   
    
    
    // ###
    
    public List<MediaplanData> getMediaPlanSubAdunitList(Integer mplid, Integer pubid, Integer adid,Integer plcid){
        
       
        
                      
        List<MediaplanData> lst = new ArrayList<MediaplanData>();
        
        List<Invtargeting> list = new ArrayList<Invtargeting>() ;
        
        if(plcid==0){
          List<Invtargeting> adulist = mediaplanDao.getMediaPlanSubAdUnitList(mplid, pubid,adid );   
            
            if(adulist.size()>0)  list.addAll(adulist);
        }else{
            List<Invtargeting> plclist = mediaplanDao.getMediaPlanSubAdUnitPlacementList(mplid, pubid, plcid);
            System.out.println("  plc list "+plclist.size());
        if(plclist.size()>0) list.addAll(plclist);
        }
        
        for (Invtargeting inv : list) {
                    
                Integer aduid = inv.getAdUnitId().getId();
                String name = inv.getAdUnitId().getName();                 
                String sdt = null;
                String edt = null;
                Integer targeting=null;
                Integer invPublishers =null;
                Integer invPlacements =null;                
                Integer invSubAdunits = null ;// mediaplanDao.getInvTarAdunitsCount(mplid, pubid, aduid);
                Double CPM = Double.valueOf(inv.getCpm().toString());                
                Integer impression = mediaplanDao.getInvTarAdunitsImpression(mplid, pubid, aduid);                
                String Status = inv.getStatus();                               
                String Style = inv.getAdUnitId().getStyle();
                String Sizes= inv.getAdUnitId().getSizes();                
                
                MediaplanData mediaplanData = new MediaplanData(aduid, name, targeting, sdt, edt, invPublishers, invSubAdunits, invPlacements, impression, CPM, Status, Style, Sizes);
               
                 mediaplanData.setAdunitId(inv.getAdUnitId().getId());
                mediaplanData.setPlacementId(inv.getPlacementId().getId());
                
                lst.add(mediaplanData);
        }
        return   lst;
    }   
    
    
    // ###
    
    
    
    
    // ### 3rd page
    
    public boolean MediaplanlineUpdate(Integer mpid, Integer mplid,  Date sdt , Date edt, Integer imp){
        
        try {
            
            boolean isUpdate = mediaplanDao.updateMediaPlanLine(mpid, mplid, sdt, edt, imp);
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        
    } 
   
    public String sendMail(){
        
        return "none";
       // SendEmail se = new SendEmail();
        //return se.sendMail(); 
    }
    
    
    
}
