/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.dao;

import com.lrl.c1.bean.MediaplanBean;
import com.lrl.c1.entity.Adunits;
import com.lrl.c1.entity.Advertiser;
import com.lrl.c1.entity.Invtargeting;
import java.util.List;

import com.lrl.c1.entity.Mediaplan;
import com.lrl.c1.entity.Mediaplanline;
import com.lrl.c1.entity.Publisher;
import com.lrl.c1.entity.Targeting;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.eclipse.persistence.internal.oxm.schema.model.Restriction;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author logic
 */
@Repository("mediaplanDao")
public class MediaPlanDAOImpl implements MediaPlanDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");

    public List<Mediaplan> FetchAllMediaPlan(int userid) {
        try {
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Mediaplan.class);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Mediaplan> FetchReqProposals() {
        Session session = sessionFactory.getCurrentSession();

        String qstr = "SELECT media.agencyId AS aid, SUM(mdplan.inventoryApproved) AS ia FROM Mediaplan as media JOIN media.mediaplanlineList as mdplan GROUP BY media.id";

        Query query = session.createQuery(qstr);

        List<Mediaplan[]> media = (List<Mediaplan[]>) query.list();
        Collection c = query.list();

        Iterator i = c.iterator();
        while (i.hasNext()) {
            System.out.println("obj =" + i.next().toString());
        }
        return query.list();

    }

    public List<Mediaplan> MediaPlanTargetting(int userid, Integer mediaplanid) {

        Session session = sessionFactory.getCurrentSession();
//        Query qry = session.createQuery("SELECT v.vendorName, c.customerName FROM Vendor v LEFT JOIN v.children c");
//        List l = qry.list();
//        Iterator it = l.iterator();
//
//        while (it.hasNext()) {
//            Object rows[] = (Object[]) it.next();
//            System.out.println(rows[0] + " -- " + rows[1]);
//        }

        try {
            String qstr = "FROM Mediaplan M";
            Query query = session.createQuery(qstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Mediaplan> FetchMediaPlanInventory(int userid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query qry = session.createQuery("SELECT COUNT(*) FROM Mediaplan mp, Mediaplanline mpl WHERE mp.id=mpl.PlanId GROUP BY mpl.PlanId");
            List l = qry.list();
            Iterator it = l.iterator();
            while (it.hasNext()) {
                Object rows[] = (Object[]) it.next();
                System.out.println(rows[0] + " -- " + rows[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MediaplanBean getPublisherCount(Integer mpid) {
        MediaplanBean mpbean = new MediaplanBean();
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "FROM Mediaplanline M WHERE M.planId = :id GROUP BY M.publisherId";
            Query query = session.createQuery(qstr).setInteger("id", mpid);
            mpbean.setPubCount(query.list().size());
            return mpbean;
        } catch (Exception e) {
            e.printStackTrace();
            mpbean.setPubCount(0);
            return mpbean;
        }
    }

    public Integer getPlacementImpression(Integer mplid, Integer pid) {
        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT SUM(I.impressionBooked) FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid);
        Integer totalImp = 0;

        ListIterator mplist = query.list().listIterator();
        try {
            totalImp = ((Long) mplist.next()).intValue();// i.getImpressionBooked();
        } catch (Exception e) {
            totalImp = 0;
            //e.printStackTrace();
        }
        return totalImp;
    }

    public Double getPlacementCPM(Integer mplid, Integer pid) {
        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT AVG(I.cpm) FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid);

        Double totalImp = 0.00;
        ListIterator mplist = query.list().listIterator();
        try {
            while (mplist.hasNext()) {
                totalImp = (Double) mplist.next();
            }
        } catch (Exception e) {
            totalImp = 0.0;
            //e.printStackTrace();
        }
        return totalImp;
    }

    // FOR JSON 
    public List secondLayer(Integer mpid) {
        try {

            List publisherlist = new ArrayList();
            Session session = sessionFactory.getCurrentSession();
            String qstr = "FROM Mediaplanline M WHERE M.planId = :pid group by M.publisherId";
            Query query = session.createQuery(qstr).setInteger("pid", mpid);

            Object[] o = new Object[2];
            ListIterator mplist = query.list().listIterator();

            JSONObject secLayer = new JSONObject();
            JSONArray arr = new JSONArray();

            while (mplist.hasNext()) {

                JSONObject pubJSON = new JSONObject();
                JSONObject pubInv = new JSONObject();

                Mediaplanline r = (Mediaplanline) mplist.next();
                Query qry = session.getNamedQuery("Publisher.findById").setInteger("id", r.getPublisherId().getId());

                List<Publisher> plist = qry.list();
                Publisher p = plist.listIterator().next();
                List l1 = new ArrayList();

                l1.add(p.getName());
                l1.add(this.getPublisherTargetingCount(mpid, p.getId()));
                l1.add(sdf.format(this.getMinStartdt(mpid, p.getId())));
                l1.add(sdf.format(this.getMaxEnddt(mpid, p.getId())));
                l1.add(this.getPublisherPlacementCount(mpid, p.getId()));
                l1.add(p.getInvtargetingList().size());
                pubInv.put("pubAdUnit", p.getInvtargetingList().size());
                l1.add(this.getPublisherImpressions(mpid, p.getId()));
                l1.add(this.getPublisherCPM(mpid, p.getId()));

                pubJSON.put("publisherId", p.getId());
                pubJSON.put("publisherName", p.getName());
                pubJSON.put("pubTar", this.getPublisherTargetingCount(mpid, p.getId()));
                pubJSON.put("pubStartDate", sdf.format(this.getMinStartdt(mpid, p.getId())));
                pubJSON.put("pubEndDate", sdf.format(this.getMaxEnddt(mpid, p.getId())));

                pubInv.put("pubPlcCount", this.getPublisherPlacementCount(mpid, p.getId()));

                pubJSON.put("pubInventory", pubInv); // 
                pubJSON.put("pubImpression", this.getPublisherImpressions(mpid, p.getId()));
                pubJSON.put("pubCPM", this.getPublisherCPM(mpid, p.getId()));
                //pubJSON.put("publisherloop", this.thirdLayer(mpid, p.getId()));

                publisherlist.add(l1);
                secLayer.append("PUBLISHER", pubJSON);

            }
            o[0] = publisherlist;
            o[1] = secLayer;
            return publisherlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List thirdLayer(Integer mid, Integer pid) {
        System.out.println(" Size of third layer entering");
        List lst = new ArrayList();

        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mid AND M.publisherId = :pid";
        Query query = session.createQuery(qstr).setInteger("mid", mid).setInteger("pid", pid);

        ListIterator mplist = query.list().listIterator();
        System.out.println(" Size of third layer" + query.list().size());
        JSONObject IIILayer = new JSONObject();
        Object[] o = new Object[2];
        while (mplist.hasNext()) {

            //   JSONObject plcJSON = new JSONObject();
            //    JSONObject plcInv = new JSONObject();

            List l = new ArrayList();
            Mediaplanline r = (Mediaplanline) mplist.next();

            l.add(r.getName());
            l.add(r.getTargetingList().size());
            l.add(sdf.format(r.getStartDate()));
            l.add(sdf.format(r.getEndDate()));
            l.add(r.getInvtargetingList().size());
            l.add(this.getPlacementImpression(r.getId(), pid));
            l.add(this.getPlacementCPM(r.getId(), pid));
            lst.add(l);

//            plcJSON.put("PlacementId", r.getId());
//            plcJSON.put("PlacementName", r.getName());
//            plcJSON.put("PlacementTar", r.getTargetingList().size());
//            plcJSON.put("PlacementStartDt", sdf.format(r.getStartDate()));
//            plcJSON.put("PlacementEndDt", sdf.format(r.getEndDate()));
//            plcJSON.put("PlacementAdUnit", r.getInvtargetingList().size());
//            plcJSON.put("PlacementImp", this.getPlacementImpression(r.getId(), pid));
//            plcJSON.put("PlacementCPM", this.getPlacementCPM(r.getId(), pid));
//            //plcJSON.put("Placementloop", this.fourthLayer(r.getId(), pid));

            // IIILayer.append("PLACEMENT", plcJSON);
            System.out.println("    3|____  " + l);
            // this.fourthLayer(r.getId(), pid); // plan line and publisherid  third layer
        }
        //System.out.println("third layer =="+lst);
        o[0] = lst;
        o[1] = IIILayer;
        return lst;
    }

    public org.codehaus.jettison.json.JSONObject fourthLayer(Integer mplid, Integer pid) {
        List lst = new ArrayList();
        //  System.out.println(" Jettison json  ");
        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT I FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.parentAdUnitId IS NULL ";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid);
        //  System.out.println("Fourth layer "+query.list().size() );
        ListIterator invtlist = query.list().listIterator();
        org.codehaus.jettison.json.JSONObject j = new org.codehaus.jettison.json.JSONObject();

        org.codehaus.jettison.json.JSONObject IVLayer = new org.codehaus.jettison.json.JSONObject();
        Object[] o = new Object[2];
        while (invtlist.hasNext()) {
            try {
                List l = new ArrayList();
                Invtargeting inv = (Invtargeting) invtlist.next();

                org.codehaus.jettison.json.JSONObject adunitJSON = new org.codehaus.jettison.json.JSONObject();

                l.add(inv.getAdUnitId().getName());
                l.add(inv.getAdUnitId().getAdunitsList().size()); // sub units

                Integer imp = this.getAdUnitImpCount(mplid, pid, inv.getAdUnitId().getId());
                Double cpm = this.getAdUnitAvgCPM(mplid, pid, inv.getAdUnitId().getId());
                l.add(imp);
                l.add(inv.getStatus());
                l.add(cpm);
                lst.add(l);

                adunitJSON.put("AdUnitID", inv.getId());
                adunitJSON.put("AdUnitName", inv.getAdUnitId().getName());
                adunitJSON.put("AdUnitInv", inv.getAdUnitId().getName());
                adunitJSON.put("AdUnitImp", imp);
                adunitJSON.put("AdUnitStatus", inv.getStatus());
                adunitJSON.put("AdUnitCPM", cpm);
                // adunitJSON.put("AdUnitloop", this.fifthLayer(mplid, inv.getAdUnitId().getId(), pid));
                IVLayer.append("ADUNITS", adunitJSON);
                IVLayer.toString(5);
                System.out.println("       4|____  " + l);
                // this.fifthLayer(inv.getAdUnitId().getId(), pid); // third layer
            } catch (JSONException ex) {
                Logger.getLogger(MediaPlanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        o[0] = lst;
        o[1] = IVLayer;
        return IVLayer;

    }

    public Object[] fifthLayer(Integer mplid, Integer aduid, Integer pid) {
        List lst = new ArrayList();
        Object[] o = new Object[2];
        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT A FROM Adunits A WHERE A.parentId = :aduid AND A.publisherId = :pid AND A.parentId NOT NULL";
        Query query = session.createQuery(qstr).setInteger("aduid", aduid).setInteger("pid", pid);

        ListIterator adulist = query.list().listIterator();

        JSONObject VLayer = new JSONObject();

        while (adulist.hasNext()) {
            List l = new ArrayList();

            JSONObject subUnitJSON = new JSONObject();

            Adunits adu = (Adunits) adulist.next();

            l.add(adu.getName());
            l.add(adu.getStyle()); // sub units
            Integer imp = this.getSubUnitImpCount(mplid, pid, adu.getId());
            l.add(imp);
            String status = this.getSubUnitStatus(mplid, pid, adu.getId());
            l.add(status); // ###
            l.add(adu.getCpm());
            lst.add(l);

            subUnitJSON.put("subUnitId", adu.getId());
            subUnitJSON.put("subUnitName", adu.getName());
            subUnitJSON.put("subUnitStyle", adu.getStyle());
            subUnitJSON.put("subUnitImp", imp);
            subUnitJSON.put("subUnitStauts", status);
            subUnitJSON.put("subUnitCPM", adu.getCpm()); // ###             
            System.out.println("          5|______  " + l);
            VLayer.append("SUBUNITS", subUnitJSON);
        }
        o[0] = lst;
        o[1] = VLayer;
        return o;

    }

    public Integer getAdUnitImpCount(Integer mplid, Integer pid, Integer aid) {

        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT SUM(I.impressionBooked) FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.adUnitId = :auid AND parentAdUnitId IS NULL";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("auid", aid);
        Integer totalImp = 0;

        ListIterator mplist = query.list().listIterator();
        try {
            totalImp = ((Long) mplist.next()).intValue();

        } catch (Exception e) {
            totalImp = 0;
            //e.printStackTrace();
        }

        return totalImp;
    }

    public Double getAdUnitAvgCPM(Integer mplid, Integer pid, Integer aid) {

        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT cpm FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.adUnitId = :auid AND parentAdUnitId IS NULL";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("auid", aid);
        Double cpm = 0.0;

        ListIterator mplist = query.list().listIterator();
        try {
            cpm = ((Double) mplist.next()).doubleValue();

        } catch (Exception e) {
            cpm = 0.0;
            //e.printStackTrace();
        }

        return cpm;
    }

    public Integer getSubUnitImpCount(Integer mplid, Integer pid, Integer aid) {

        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT I.impressionBooked FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND adUnitId = :auid";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("auid", aid);
        Integer totalImp = 0;

        ListIterator mplist = query.list().listIterator();
        try {
            totalImp = ((Long) mplist.next()).intValue();

        } catch (Exception e) {
            totalImp = 0;
            //e.printStackTrace();
        }

        return totalImp;
    }

    public String getSubUnitStatus(Integer mplid, Integer pid, Integer aid) {

        Session session = sessionFactory.getCurrentSession();
        String qstr = "SELECT I.status FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND adUnitId = :auid";
        Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("auid", aid);

        String status = "";

        ListIterator mplist = query.list().listIterator();
        try {
            status = mplist.next().toString();

        } catch (Exception e) {
            status = "";
            //e.printStackTrace();
        }
        return status;
    }

    public boolean DeleteMediaPlan(Integer mpid) {
        Session session = sessionFactory.getCurrentSession();
        String qstr = "DELETE Mediaplan where id = :mpid";
        Query query = session.createQuery(qstr).setInteger("mpid", mpid);
        int result = query.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean DeletePublisherLine(Integer mpid, Integer mplid, Integer pid) {
        Session session = sessionFactory.getCurrentSession();
        String qstr = "DELETE Mediaplanline where publisherId = :pid AND planId = :mpid AND id= :mplid";
        Query query = session.createQuery(qstr).setInteger("pid", pid).setInteger("mpid", mpid).setInteger("mplid", mplid);
        int result = query.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean DeleteAdunits(Integer mplid, Integer pid, Integer auid) {
        Session session = sessionFactory.getCurrentSession();
        String qstr = "DELETE Invtargeting where publisherId = :pid AND planLineId = :mplid AND adUnitId = :auid "; // AND parentAdUnitId = :auid";
        Query query = session.createQuery(qstr).setInteger("pid", pid).setInteger("mplid", mplid).setInteger("auid", auid);
        int result = query.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean DeleteSubAdunits(Integer mplid, Integer pid, Integer auid) {
        Session session = sessionFactory.getCurrentSession();
        String qstr = "DELETE AdUnits where publisherId = :pid AND id = :auid";
        Query query = session.createQuery(qstr).setInteger("pid", pid).setInteger("mplid", mplid).setInteger("auid", auid);
        int result = query.executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    // ###USED First row start   
    public List<Mediaplan> getMediaPlanList(String mpname,String adname,String status) {
        try {
            
            System.out.println("  mp name == "+mpname+   adname+  status);
            
            if(mpname.equals("0"))  mpname = ""; 
            if(adname.equals("0")) adname = "";
            if(status.equals("0")) status = "";
           
            
            
          //  Criteria c1 = sessionFactory.getCurrentSession().createCriteria(Advertiser.class).add(Restrictions.like("name", mpname));
            
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Mediaplan.class,"media").add(Restrictions.like("name", "%"+mpname+"%")).add(Restrictions.like("status", "%"+status+"%"));
            criteria.createAlias("media.advertiserId", "ad").add(Restrictions.like("ad.name", "%"+adname+"%") );
            System.out.println("  mp name == "+mpname+   adname+  status);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //### USED
    public Integer getMediaPlanTargetingCount(Integer mpid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "";
            Query query = null;

            qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid ";
            query = session.createQuery(qstr).setInteger("mpid", mpid);
            List<Mediaplanline> mplist = query.list();
            Integer target = 0;

            for (Mediaplanline mpl : mplist) {
                target += mpl.getTargetingList().size(); // point to targeting table               
            }

            return target;

        } catch (Exception e) {
            return 0;
        }
    }

    //### USED
    public Integer getMediaPlanPublishersCount(Integer mpid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "";
            Query query = null;

            qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid GROUP BY M.publisherId";
            query = session.createQuery(qstr).setInteger("mpid", mpid);
            Integer publisher = query.list().size();
            return publisher;

        } catch (Exception e) {
            return 0;
        }
    }

    //### USED
    public Integer getMediaPlanPlacementsCount(Integer mpid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "";
            Query query = null;

            qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid";
            query = session.createQuery(qstr).setInteger("mpid", mpid);
            Integer placements = query.list().size();
            return placements;

        } catch (Exception e) {
            return 0;
        }
    }

    //### USED
    public Integer getMediaPlanAdunitsCount(Integer mpid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "";
            Query query = null;

            qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid ";
            query = session.createQuery(qstr).setInteger("mpid", mpid);
            List<Mediaplanline> mplist = query.list();
            Integer adUnits = 0;

            for (Mediaplanline mpl : mplist) {
                adUnits += mpl.getInvtargetingList().size(); // point to targeting table               
            }

            return adUnits;

        } catch (Exception e) {
            return 0;
        }
    }

    //### USED
    public Double getMediaPlanCPM(Integer mpid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "";
            Query query = null;

            qstr = "SELECT AVG(M.CPM) FROM Mediaplanline M WHERE M.planId = :mpid GROUP BY M.planId ";
            query = session.createQuery(qstr).setInteger("mpid", mpid);
            Double cpm = 0.0;
            if (query.list().size() > 0) {
                cpm = Double.valueOf(query.list().get(0).toString());
            }

            return cpm;

        } catch (Exception e) {
            return 0.0;
        }
    }

    // ###USED second row start  
    public List<Mediaplanline> getMediaPlanPublisherList(Integer mpid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "";
            Query query = null;
            qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid GROUP BY M.publisherId";
            query = session.createQuery(qstr).setInteger("mpid", mpid);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // ###USED
    public Integer getPublisherPlacementCount(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = null;
            String qstr = "";
            if (mpid != 0) {
                qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid";
                query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);
            } else {
                qstr = "SELECT M FROM Mediaplanline M WHERE M.publisherId = :pid";
                query = session.createQuery(qstr).setInteger("pid", pubid);
            }
            return query.list().size();
        } catch (Exception e) {
            return 0;
        }
    }

    // ##USED
    public Integer getPublisherAdunitCount(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = null;
            String qstr = "";
            qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid";
            query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);

            List<Mediaplanline> mplist = query.list();
            Integer invtarget = 0;

            for (Mediaplanline mpl : mplist) {
                invtarget += mpl.getInvtargetingList().size();
            }

            return invtarget;

        } catch (Exception e) {
            return 0;
        }
    }
    //### USED

    public Date getMinStartdt(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT MIN(M.startDate) AS dt FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid";
            Query query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);

            //return sdf.parse(query.list().get(0).toString().substring(0, 19));
            return (Date) query.list().get(0);

        } catch (Exception e) {
            System.out.println(" " + e);
            return null;
        }

    }
    //### USED

    public Date getMaxEnddt(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT MAX(M.endDate) AS dt FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid";
            Query query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);
            // System.out.println(query+" Max Date ==  " + query.list().get(0));
            return (Date) query.list().get(0);
        } catch (Exception e) {

            return null;
        }

    }
    //### USED

    public Integer getPublisherTargetingCount(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid";
            Query query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);

            Integer tsize = 0;
            ListIterator mplist = query.list().listIterator();
            while (mplist.hasNext()) {
                Mediaplanline r = (Mediaplanline) mplist.next();
                tsize += r.getTargetingList().size();
            }
            return tsize;
        } catch (Exception e) {
            return 0;
        }
    }
    //### USED

    public Integer getPublisherImpressions(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT SUM(M.inventoryApproved) As app FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid GROUP BY M.publisherId";
            Query query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);

            if (query.list().size() > 0) {
                return Integer.valueOf(query.list().get(0).toString());
            } else {
                return 0;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return 0;
        }

    }
    //### USED

    public Double getPublisherCPM(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = null;
            String qstr = "";
            if (mpid != 0) {
                qstr = "SELECT AVG(M.cpm) AS price FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid GROUP BY M.publisherId";
                query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);
            } else {
                qstr = "SELECT AVG(M.cpm) AS price FROM Mediaplanline M WHERE M.publisherId = :pid GROUP BY M.publisherId";
                query = session.createQuery(qstr).setInteger("pid", pubid);

            }

            if (query.list().size() > 0) {
                return Double.valueOf(query.list().get(0).toString());
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return 0.0;
        }
    }

    //### USED 
    public String getPublisherStatus(Integer mpid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = null;
            String qstr = "";
            String status = "";
            qstr = "SELECT COUNT(M),M.status FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid GROUP BY M.status";
            query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pubid);
            List lst = query.list();
            int approvedCount = 0;
            int size = 0;
            if (lst.size() > 0) {
                ListIterator i = lst.listIterator();
                while (i.hasNext()) {
                    List clst = (List) i.next();
                    if (clst.get(1) == "Approved") {
                        approvedCount = Integer.parseInt(clst.get(0).toString());
                    }
                    size += Integer.parseInt(clst.get(0).toString());
                }
                status = (approvedCount + " Of " + size + " Approved ");

                return status;
            } else {
                return "";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return "";
        }
    }

    // ### USED
    public List<Mediaplanline> getMediaPlanPlacementList(Integer mpid, Integer pid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT M FROM Mediaplanline M WHERE M.planId = :mpid AND M.publisherId = :pid";
            Query query = session.createQuery(qstr).setInteger("mpid", mpid).setInteger("pid", pid);

            return query.list();

        } catch (Exception e) {
            return null;
        }
    }

    //### USED
    public List<Targeting> getPlacementTargetingCount(Integer mplid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT T FROM Targeting T WHERE T.mediaPlanId = :mplid";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid);
            System.out.println("size of tartgeting " + query.list().size());
            return query.list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //### USED
    public Integer getPlacementAdUnitsCount(Integer mplid, Integer pubid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT I FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pubid";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pubid", pubid);

            return query.list().size();

        } catch (Exception e) {
            return 0;
        }
    }

    //### USED
    public List<Invtargeting> getMediaPlanAdUnitList(Integer mplid, Integer pid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT I FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.parentAdUnitId IS NULL ";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid);

            return query.list();

        } catch (Exception e) {
            System.out.println(" Exception in ee ==="+e);
            e.printStackTrace();
            return new ArrayList<Invtargeting>();
            
        }
    }
    
    //### USED
    @Override
    public List<Invtargeting> getMediaPlanAdunitsPlacementList(Integer mplid, Integer pid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT I FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.placementId IS NOT NULL ";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid);

            return query.list();

        } catch (Exception e) {
            return new ArrayList<Invtargeting>();
        }
    }

    //### USED
    public Integer getInvTarAdunitsCount(Integer mplid, Integer pid, Integer aduid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT I FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.adUnitId = :aduid";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("aduid", aduid);
            return query.list().size();

        } catch (Exception e) {
            return null;
        }

    }

    //### USED
    public Integer getInvTarAdunitsImpression(Integer mplid, Integer pid, Integer aduid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT SUM(I.impressionBooked) FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId = :pid AND I.adUnitId = :aduid";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("aduid", aduid);
            if (query.list().size() > 0) {
                return Integer.valueOf(query.list().get(0).toString());
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    //### USED
    public List<Invtargeting> getMediaPlanSubAdUnitList(Integer mplid, Integer pid, Integer aduid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT I FROM Invtargeting I WHERE I.planLineId = :mplid AND I.publisherId =:pid AND I.parentAdUnitId =:aduid";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("aduid", aduid);

            return query.list();

        } catch (Exception e) {
            return null;
        }
    }
    
    //### USED
    public List<Invtargeting> getMediaPlanSubAdUnitPlacementList(Integer mplid, Integer pid, Integer plcid) {

        try {
            Session session = sessionFactory.getCurrentSession();
            String qstr = "SELECT I FROM Invtargeting I,Adunitsplacements A WHERE I.planLineId = :mplid AND I.publisherId =:pid AND I.adUnitId =A.adUnitId AND A.placementId = :plcid";
            Query query = session.createQuery(qstr).setInteger("mplid", mplid).setInteger("pid", pid).setInteger("plcid", plcid);

            return query.list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
   
    // ### USED 
   
    public boolean updateMediaPlanLine(Integer mpid, Integer mplid, Date stdt, Date enddt, Integer impr) throws DataAccessException{       
              try {
            Session session = sessionFactory.getCurrentSession();
              Mediaplanline mpl = (Mediaplanline) session.get(Mediaplanline.class, mplid);
              
                  System.out.println(" MPL ID is  "+mpl.getId());  
              
              mpl.setStartDate(stdt);
              mpl.setEndDate(enddt);
              mpl.setInventoryApproved(impr);
              session.update(mpl);
              return true;
                } catch (Exception e) {
                    System.out.println("Exception in End dt   "+enddt);
                    e.printStackTrace();
                    return false;
                }
              
              //getHibernateTemplate().save(mpl);
             
    }
    
    
    
} // end class

