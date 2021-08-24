package cn.zwqh.springboot;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.zwqh.springboot.model.UserEntity;

@Controller
public class testClass {
	
	@Autowired
	private RedisTemplate<String, String> strRedisTemplate;
	@Autowired
	private RedisTemplate<String, Serializable> serializableRedisTemplate;
    @GetMapping("/doHello")   //请求的映射路由
    public String home(Model model){

        return "test";
    }
    
    @GetMapping("/doRegister")  
    public String doRegister(@RequestParam("tel") String tel, @RequestParam("userName") String userName, @RequestParam("employer") String employer, 
    		@RequestParam("workNo") String workNo, @RequestParam("haveCertificate") String haveCertificate, @RequestParam("carType") String carType, 
    		@RequestParam("remakes") String remakes,  Model model){
		UserEntity user=new UserEntity();
		user.setTel(tel);
		user.setUserName(userName);
		user.setEmployer(employer);
		user.setWorkNo(workNo);
		user.setHaveCertificate(haveCertificate);
		user.setCarType(carType);
		user.setRemakes(remakes);
		user.setAuditStatus("还未审核");
		serializableRedisTemplate.opsForValue().set(workNo, user);	
		model.addAttribute("user",user); 
        return "result";
    } 
    @GetMapping("/doManage")   
    public String doManage(Model model){
	
    	List<UserEntity> userList = new ArrayList<>();
    	Set<String> keys = serializableRedisTemplate.keys("*");

    	for(String key : keys) {
    		UserEntity user = (UserEntity) serializableRedisTemplate.opsForValue().get(key);
    		userList.add(user);
    	}
		model.addAttribute("userList",userList); 
		Date startDay=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("nowDate",df.format(startDay)); 
        return "managerPage";
    }
    @GetMapping("/doGotoSearch")  
    public String doGotoSearch(Model model){

        return "search";
    }
    @GetMapping("/doIndexPage")  
    public String doIndexPage(Model model){

        return "indexPage";
    }
    @GetMapping("/doSearch")  
    public String doSearch(Model model, @RequestParam("workNo") String workNo){
    	
    	UserEntity user = (UserEntity) serializableRedisTemplate.opsForValue().get(workNo);
    	if(user == null) {
    		 return "searchResultError";
    	}
    	model.addAttribute("user",user); 
        return "searchResult";
    }
    @RequestMapping("/doDownload")  
    public @ResponseBody String doDownload(Model model,HttpServletResponse response) throws Exception{
    	response.setContentType("application/binary;charset=UTF-8");
    	List<UserEntity> userList = new ArrayList<>();
    	Set<String> keys = serializableRedisTemplate.keys("*");

    	for(String key : keys) {
    		UserEntity user = (UserEntity) serializableRedisTemplate.opsForValue().get(key);
    		userList.add(user);
    	}
    	 try{
    		 ServletOutputStream out=response.getOutputStream();
    		 Date date = new Date();
    		 response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(date.toString() + ".xls", "UTF-8"));
             HSSFWorkbook workbook = new HSSFWorkbook();
             
             // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
             HSSFSheet hssfSheet = workbook.createSheet("sheet1");
             
             // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
             
             HSSFRow row = hssfSheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
             HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
             
             //居中样式
             hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
             String[] titles = { "姓名", "电话", "单位", "工号", "是否有国家证", "牵引车/叉车/天车选择", "备注", "是否通过审核", "管理人员备注"}; 
             HSSFCell hssfCell = null;
             for (int i = 0; i < titles.length; i++) {
                 hssfCell = row.createCell(i);//列索引从0开始
                 hssfCell.setCellValue(titles[i]);//列名1
                 hssfCell.setCellStyle(hssfCellStyle);//列居中显示                
             }
             
                 for (int i = 0; i < userList.size(); i++) {
                     row = hssfSheet.createRow(i+1);                
                     UserEntity user = userList.get(i);
                     
                     // 第六步，创建单元格，并设置值
                     String  id = null;
      
                    row.createCell(0).setCellValue(user.getUserName());
                    row.createCell(1).setCellValue(user.getTel());
                    row.createCell(2).setCellValue(user.getEmployer());
                    row.createCell(3).setCellValue(user.getWorkNo());
                    row.createCell(4).setCellValue(user.getHaveCertificate());
                    row.createCell(5).setCellValue(user.getCarType());
                    row.createCell(6).setCellValue(user.getRemakes());
                    row.createCell(7).setCellValue(user.getAuditStatus());
                    row.createCell(8).setCellValue(user.getManageRemakes());

                 }

             // 第七步，将文件输出到客户端浏览器
             try {
                 workbook.write(out);
                 out.flush();
                out.close();
 
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }catch(Exception e){
             e.printStackTrace();
            throw new Exception("导出信息失败！");
            
            }
        return "searchResult";
    }
    @GetMapping("/doUpdateData")  
    public String doUpdateData(Model model, @RequestParam("workNo") String workNo){
    	
    	UserEntity user = (UserEntity) serializableRedisTemplate.opsForValue().get(workNo);

    	model.addAttribute("user",user); 
        return "updateData";
    }
    @GetMapping("/doUpdateDataSubmit")  
    public String doUpdateDataSubmit(@RequestParam("tel") String tel, @RequestParam("userName") String userName, @RequestParam("employer") String employer, 
    		@RequestParam("workNo") String workNo, @RequestParam("haveCertificate") String haveCertificate, @RequestParam("carType") String carType, 
    		@RequestParam("remakes") String remakes,  String auditStatus,  String manageRemakes, String startDate,String endDate, Model model){
		UserEntity user=new UserEntity();
		user.setTel(tel);
		user.setUserName(userName);
		user.setEmployer(employer);
		user.setWorkNo(workNo);
		user.setHaveCertificate(haveCertificate);
		user.setCarType(carType);
		user.setRemakes(remakes);
		if(!"还未审核".equals(auditStatus)) {
			user.setAuditStatus(auditStatus);
			if("".equals(startDate)) {
				Date startDay=new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				user.setStartDate(df.format(startDay));
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(startDay);
		        rightNow.add(Calendar.YEAR,3);
		        Date endDay=rightNow.getTime();
		        user.setEndDate(df.format(endDay));
			}
		}
		if(!"".equals(startDate)) {
			user.setStartDate(startDate);
		}
		if(!"".equals(endDate)) {
			user.setEndDate(endDate);
		}
		if(!"".equals(manageRemakes)) {
			user.setManageRemakes(manageRemakes);
		}
		
		serializableRedisTemplate.opsForValue().set	(workNo, user);	
		model.addAttribute("user",user); 
        return "resultManage";
    } 
}
