/**
 * Created by IntelliJ IDEA.
 * User: zly
 * Date: 12-6-6
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */

/**
 * 验证是否数字
 * */
   function valNum(val)
    {
 	  return /^[0-9]*$/.test(val);
    }
   
   
   /**
    * 验证是否输入的是英文或者数字
    * */
      function valNumAndStr(val)
      {
    	  return /^[0-9a-zA-z]+$/.test(val);
      }
      
      /**
       * 去左空格
       **/
       function ltrim(s)
       {
    	   return s.replace(/(^\s*)/,"");
       }
       /**
        * 去右空格
        **/
       function rtrim(s)
       {
    	   return s.replace(/(\s*$)/,"");
       }
       
       /**
        * 去左右空格
        **/
       function trim(s)
       {
    	   return rtrim(ltrim(s));
       }
       
       
       /**
        * 验证非法字符
        * */
       function valStr(str){
    	   var valStr="·`。，^\\!@/|/;?'\"【】［］＇“’、？（）￥！<>，《》×＆……～＠＃％…（）——－＝＋１２３４５６７８９０ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
    	   if(valStr.indexOf(str)!=-1)
			 {
		 	    return false;
			 }
    	   return true;
       }
       
       /**
        * 
        * 验证字符串中是否有特殊字符，如果有的话则删除特殊字符
        * */
       function validateStrAndGetStr(str)
       {
    	   var arr=str.split("");
		   var tmp="";
		   for(var i=0;i<arr.length;i++)
		   {
			    var temp="";
		 	    if(valStr(arr[i]))
				{
		 	    	temp=arr[i];
				}
				 tmp+=temp;
			}
			return tmp;
       }
       
       /**
        * 只能输入数字+字母
        * 验证字符串中是否有特殊字符，如果有的话则删除特殊字符
        * */
       function validateStr(str)
       {
    	   var arr=str.split("");
		   var tmp="";
		   for(var i=0;i<arr.length;i++)
		   {
			    var temp="";
		 	    if(valStr(arr[i]) && valNumAndStr(arr[i]))
				{
		 	    	temp=arr[i];
				}
				 tmp+=temp;
			}
			return tmp;
       }
       
       
       