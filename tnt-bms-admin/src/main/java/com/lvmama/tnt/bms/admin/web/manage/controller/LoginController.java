package com.lvmama.tnt.bms.admin.web.manage.controller;
import com.lvmama.bms.core.commons.utils.Md5Encrypt;
import com.lvmama.tnt.bms.admin.web.manage.constant.SessionUtil;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserPO;
import com.lvmama.tnt.bms.admin.web.manage.service.IUserService;
import com.lvmama.tnt.bms.admin.web.util.BeanCopyUtils;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController extends AbstractController{

	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private IUserService userService;

	/**
	 * 仪表板页面
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/main/dashboard")
	public String dashboard(Model model) {
		return "dashboard";
	}

	@RequestMapping("/")
	public String index() {
		return "redirect:/login.html";
	}


	@ResponseBody
	@RequestMapping("/asyncLogin")
	public ResponseDTO asyncLogin(@RequestBody UserDTO userDTO, HttpSession session) {
		ResponseDTO responseDTO = returnSuccess();
		try {
			UserPO userPO = userService.findByUserName(userDTO.getUserName());
			if (userPO == null) {
				return returnError("该用户不存在");
			} else if (!userPO.getPassword().equals(Md5Encrypt.md5(userDTO.getPassword()))) {
				return returnError("用户名/密码错误");
			}
			UserDTO user = new UserDTO();
			BeanCopyUtils.copyBean(userPO, user);
			user.setResourceDTOS(userService.findResourceByRoles(user.getRoleCodes()));
			user.handleAccessUri();
			SessionUtil.setUser(user, session);
			return responseDTO;
		} catch (Exception e) {
			logger.error("", e);
			responseDTO = returnError();
		}
		return responseDTO;
	}


	@RequestMapping("/logout")
	public void logout(HttpServletResponse response, HttpSession session) {
		try {
			SessionUtil.removeUser(session);
			response.sendRedirect("login.html");
		} catch (IOException e) {
			logger.error("",e);
		}
	}

	@ResponseBody
	@RequestMapping("/changePassword")
	public ResponseDTO changePassword(String newPassword, HttpSession session) {
		ResponseDTO responseDTO = returnSuccess();
		UserDTO userDTO = SessionUtil.getUser(session);
		UserPO po = new UserPO();
		po.setId(userDTO.getId());
		po.setPassword(Md5Encrypt.md5(newPassword));
		userService.update(po);
		return responseDTO;
	}

}

