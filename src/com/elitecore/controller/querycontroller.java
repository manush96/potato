package com.elitecore.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.dto.querydto;
import com.elitecore.dto.userdto;
import com.elitecore.model.Query;
import com.elitecore.model.SearchKeyWord;
import com.elitecore.model.queryin;
import com.elitecore.services.queryservices;
import com.elitecore.services.transfer;
import com.elitecore.services.userservices;

@Controller
public class querycontroller {
	private userservices userService;

	@Autowired
	queryservices services;
	@Autowired
		public querycontroller(userservices userService) {
			this.userService = userService;
		}
	@RequestMapping(value="/viewquery.html*")
	public ModelAndView getPage(
	@RequestParam(value="page", required=false) int pageid, HttpSession session)
	{
		session.setAttribute("page_id",pageid);
		int total=5;
		if(pageid==1){}
		else
		{
			pageid=(pageid-1)*total+1;
		}
		//System.out.println("hello-MUMBAI");
		int result=services.getcount();
		int no;
		if(result%5 == 0)
			no = (result/5);
		else
			no = (result/5)+1;
		
		List<Query> list=services.getbypage(pageid, total);
		ModelAndView model=new ModelAndView();
		model.addObject("querydto",new querydto());
		model.addObject("list",list);
		model.addObject("count", no);
		model.setViewName("pagination");
		return model;	
	}
	
	
	@RequestMapping(value="/SearchFilter.html*")
	public ModelAndView getFilter(
	@RequestParam(value="page", required=false) int pageid, SearchKeyWord key)
	//@RequestParam(value="keyword", required=false) String keyword
	{
		int total=5;
		if(pageid==1){}
		else
		{
			pageid=(pageid-1)*total+1;
		}
		List<Query> list=services.getbykeyword(key.getKeyword(), pageid, total);
		return new ModelAndView("pagination","list",list);
	}
	
	@RequestMapping(value="/AddQuery.html*",method=RequestMethod.POST)
	public String getFilter(  @ModelAttribute querydto querydto,
									@RequestParam(value="page", required=false) int pageid)
	{
		
		Query query=transfer.querytrans(querydto);
		List<queryin> list=transfer.queryintrans(querydto);
		try {
			services.addQuery(query);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			services.queryparam(list);			

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "redirect:profile.html";
		
		
	}
	
	@RequestMapping(value="/gridview.html")
	public ModelAndView getgrid()
	{
		return new ModelAndView("grid","Query", new Query());
	}
	@RequestMapping(value="/multidelete.html", method=RequestMethod.POST)
	public String multidelete(@RequestParam("ids") String ids)
	{
		services.multidelete(ids);
		return "success";
	}
	
	@RequestMapping(value="/EditQuery.html")
	public String Editquery(@ModelAttribute Query Query)
	{
		services.Updatequery(Query);
		
		return "redirect:profile.html";		//return new ModelAndView("pagination","list",new list());
	}
	
	@RequestMapping(value="/SingleDelete.html", method=RequestMethod.POST)
	public ModelAndView Editquery(@RequestParam("id") int id)
	{
		services.Dltquery(id);
		return new ModelAndView("grid","Query", new Query());
	}
	@RequestMapping(value="/keyword.html")
	public ModelAndView search()
	{
		return new ModelAndView("SearchFilter","SearchKeyWord",new SearchKeyWord());
	}
	@RequestMapping(value="/SearchFilter.html*")
	public ModelAndView getFilter(
	@RequestParam(value="page", required=false) int pageid, SearchKeyWord key, HttpSession session)
	//@RequestParam(value="keyword", required=false) String keyword
	{
		int total=5;
		if(pageid==1){}
		else
		{
			pageid=(pageid-1)*total+1;
		}
		int result=services.getcount();
		int no;
		if(result%5 == 0)
			no = (result/5);
		else
			no = (result/5)+1;
		List<Query> list=services.getbykeyword(key.getKeyword(), pageid, total);
		session.setAttribute("page_id",pageid);
		ModelAndView model=new ModelAndView();
		
		model.addObject("list",list);
		model.addObject("count", no);
		model.setViewName("filterpagination");
		return model;
		
	}
}
