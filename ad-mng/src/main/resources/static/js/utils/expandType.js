var ExpandType = {

		"SOURCE" : 1, //展开来源
		"CHANNEL_GROUP" : 2, //展开渠道分组
		"SOUCHANNELRCE" : 3, //展开渠道
		"ADVERT_PLACE" : 4, //展开广告位
		"ADVERT" : 5, //展开广告
		"ADVERT_CHILD" : 6, //展开广告子站

		
		
		/** 获取相应数据库查询字段 */
		"getColumnById" : function(angleId, id) {
			
			if (angleId==0 && id == 2) {
	            return "user_source_id";
	        } else if (angleId==0 && id == 3) {
	            return "channel_group_id";
	        } else if (angleId==0 && id == 4) {
	            return "channel_info_id";
	        } else if (angleId==0 && id == 5) {
	            return "advert_place_id";
	        } else if (angleId==0 && id == 6) {
	            return "advert_id";
	        }
	        
	        else if(angleId==1 && id == 2) {
	        	return "game_id";
	        } 
			
	        else if(angleId==2 && id == 2) {
	        	return "flash_id";
	        } 
			
			return null;
		},
		
		
		
		/** 获取相应列名 */
		"getColumnNameById" : function(angleId, id) {
			if(angleId==0 && id == 1) {
				return "用户来源";
			} else if(angleId==0 && id == 2) {
				return "渠道分组";
			} else if(angleId==0 && id == 3) {
				return "渠道";
			} else if(angleId==0 && id == 4) {
				return "广告位";
			} else if(angleId==0 && id == 5) {
				return "广告";
			} else if(angleId==0 && id == 6) {
				return "子站";
			}
			
			else if(angleId==1 && id == 1) {
	        	return "游戏";
	        } 
			else if(angleId==1 && id == 2) {
				return "区服";
			} 
			
			else if(angleId==2 && id == 2) {
	        	return "渠道";
	        } 
		},
		
		
		"end" : null
};