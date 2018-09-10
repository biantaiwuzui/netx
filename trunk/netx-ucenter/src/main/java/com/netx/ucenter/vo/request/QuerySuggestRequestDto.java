package com.netx.ucenter.vo.request;

import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.enums.UserSuggestStatus;
import com.netx.ucenter.util.IsNumber;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class QuerySuggestRequestDto extends CommonListDto {

    @ApiModelProperty("状态：\n" +
            "     REFUSE：无效 \n" +
            "     ACCEPT：有效 \n" +
            "     SHELVE：搁置 \n" +
            "     WAITING：未审核")
    private UserSuggestStatus status;
    /**
     * 前端数据
     */
    @Pattern(regexp = "^[\\d]*$||^\\s$", message = "请输入数字！")
    @Size(max = 11, message = "请输入正确的网号或号码长度!")
    private String numCheck;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}|9[2-8]\\d{9}$|(\\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$", message = "手机号码不正确!")
    private String mobile;

    /**
     * 网号
     */
    @Pattern(regexp = "[\\d]{10}||^\\s$", message = "网号不正确或长度不对！")
    private String userNumber;


    public UserSuggestStatus getStatus() {
        return status;
    }

    public void setStatus(UserSuggestStatus status) {
        this.status = status;
    }

    public String getNumCheck() {
        return numCheck;
    }

    public void setNumCheck(String numCheck) {
        this.numCheck = numCheck.trim();
        if (IsNumber.isNumeric(numCheck) == true) {
            if (numCheck.length() == 10||numCheck.equals("")) {
                userNumber = numCheck.trim();
            } else if (numCheck.length() == 11) {
                mobile = numCheck.trim();
            } else if (1 < numCheck.length() && numCheck.length() < 10) {
                userNumber = numCheck.trim();
            }
        }
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    @Override
    public String toString() {
        return "QuerySuggestRequestDto{" +
                "status=" + status +
                '}';
    }


}
