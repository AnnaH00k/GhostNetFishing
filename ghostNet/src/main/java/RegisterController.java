import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterController implements Serializable {
    
    private static final long serialVersionUID = 1L;


	@Inject
    private PersonenListe personenListe;
    
    
    private String passwortBestaetigung;
    private String fehlermeldung;
    
    private static final List<String> DEFAULT_IMAGES = Arrays.asList(
    		"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUSEhIVFhUVFRUWFRUVFRUVFxYVFRcWFhUVFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGhAQGi0fHyUtLS0tLS0tLS0tLSstLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAMEBBQMBIgACEQEDEQH/xAAbAAABBQEBAAAAAAAAAAAAAAADAAECBAUGB//EADwQAAICAQIDBwIEBAMIAwAAAAECABEDEiEEMUEFEyJRYXGBMpEGI6GxUsHR8BRCYhUzQ3KCkuHxB1Oi/8QAGgEAAwEBAQEAAAAAAAAAAAAAAAECAwQFBv/EACERAQADAAIDAAMBAQAAAAAAAAABAhEDIRIxQQQUUWEy/9oADAMBAAIRAxEAPwDnUWWkXaCQQ6CfSPETVYdBBoIZRAklEncjHsRBK4rjR9MNGSe4hGqOI0ykBFFckIBGpMSMcQI8QEcR4A0cCK44gDRVHigDVFUeMIAhHiiiM0UeKANHEaPEcGMUREUFHMaPGMRoERSVRRGwkEMgkEEMqzRCaCPkygRMaEz82WzUi9vGF0p5SI/E3yk8OWU/aIv5Tivzy7K8MNUcQLl0EETK4PDq3miq1N+KZt2x5YivRzFFcep0w5JPHBkRHECSAiEUeAPHqSTGTyBPsIX/AAjg0VIHVjso+T/KTNoj2qKzPqAtEQlx+z3WrB8RAFaeR/zUSCR8Q7dmqMipztbPjAYV/prkfOZzzVj61jgvPxmRVNxUw2V0nUNjpXUv/cBYPyIXMiJ3WPQtsaJCZWJoXuwPh92P3kfsR/Gn6s/1zwW+UkMLcgp+xnS5nZB+WoZKI0ppLH0W6UV6yfDYMjGwKVlBKNWux/EwaiPSqEX7H+H+r/rmU4ZzyU+XLr5e8I3AZBzRt9+XTz2mzw2BrcMEIYMrZVBwEadlRAbLe91Cd6iovfk3qBxAJlJUAV+YylgW9dhJn8hcfix/XOviI5qR7giQM6RsWUqWbGoyBj3dBcvgNfT9JII3IJ2357QHaPZyl0CruwOoqNtvLegbvbcyq88fYTb8WfksGNLHGcI2NirDl1o0ZXm8TE9w5piYnJKKKKAKKNFAz1FGiiNj4xLCiBSHqWkHjDQmQuSzNvLi1Sn/AICjOfm1vwgYYVcNtLKcHLOLFU5q8E2ntvbmisH4fHQhCY1x531rFY6cN7zaTgx4wj3LQeOIbg+HORgo+/lNxOzseFQSwLjc2CBv9HOtO46zO/JFfbTj4bX9Mnhez8mQEooNc7ND+v2E1OG7GUeLJkFdF0tf2F37fvDLkCPjx+A6gxbVlOvz8CVbknmduUimTQ2Tvgq42YLjvK+RmJG40VWPpsOnOctueZ9O2n41a++1od2hRS2xvTtQB6atufptFxObum3Phos+XJopVHTdl0/CmMcIWgwQ41B1ZMj06+RQBdIHyJZThcmsEW2MJ4mOUaa6Wn+b3mWw2zFDiOLxhlytoXGarKzr4ieSrbVvflvI9pZFV1c5GVVXUygZmsagL/LbSd2HNT+kvInd624jJi7ssoxWNGm9grEmib5VUHwzPjUd9mway7AmqDISSqrbDxVp336xeUfTyVPtBdbKMjJ3R5YyrW7gFqJ1BapSaI6SPEvkrEUyY8ROQDS9eNd/y13+qvK+Ut5EcHIcihkVg2JU+ohQDRvrqBiUNkxDJjZ8TNpasq3pAPiUpexIsbekNjBiXHcBw74iSm2M62RAx8Q8WyJuSbuq3uNj4ZsmW82NNOPS2HIpYNuPEGXajty5faFwcBiXI2dKDuFDGzTaeW3LrVwXEYTk1I+Vgdasug6GUA3RrmNqMjy1URg/FAZ2fFl4dSF0lMmRQ6G+dC7DD1rmJJcIW8agY8YUd24Ooht7GgigBt1MDxnH5FdBjUPjJIyENRTyNdRdgzMyYcQDcPqfxq7G3ctpJ3pz6nl0jgpnGth4pA+IZGRmUs2orTaaotjHQ7geW8kO1U0ayAVAc7IUJDeLlfOtj6+U5p+NYfQoKBPA4NkkWCv6DeA4DiXyHHka0oFmQ+bCqPtvKyUzaHVcRwqsiurMQcaqEc3YAsE3vqo0ZzHF4NJutjdelcxNQ8TjzrjcNfduShBI8VFDfn1ExD2gMjsu/wBKst+RA/mf1m/DOS5vyMmCjRGNOpxHuIxojEoooooGy1hhArDAyyExC5PLjIgRkreXmcMsi05LStdqq3FERHEpnJR4kFnyiqNJ5JFJIAFk8qkblvgVYW6VaUfFy/ugZNrZGqrXynG12Jh7tXbRbDUCSfCCu1X/AH1lfi3fMu642dSCpaygcf5io51vXX2kOL48nwDV4jWmuocksfT6f+31gOzmGNq8no+dk2b+ZwXmZnZenSIiMhqY8JyBHTIiM1A5Ai62C/Uqq/Lr51N08MBp7nQh1FsjaVZWAq1YbEMR13qpR4LOS7awujbuyOYNeK5Q4/jCpfGqA+JWYEAK4Y7sDyJHUc+XmJExK9hqcQmJDkBVm7wa2WyysRSlVBNKdh4dpWPGLvg7r8o46OwAo7FOdg1MvN2pqLKBRG91sb6j7SjwXF5W1d4tUdt7tfOLxmSm8Ogyr+XgxLw4bFsrq5U90ir4djercAbGQ4wlsyI3DK+PSzd4dJCMKpdJ33HWZ3Z/GqqNea1XUSzEeEbk79AIbhEy93pfLrbfx0F2J8Ow2iyYOLRK7g7zvGbKoASxi7t2IZCBs6bC9vX0kUdXVeI7p9eggKwpgrUWUrddBAdncPkSy+Vn23ugB50BJ4uNcoz5EKBdRC3qYovUgdT5C4sPS4fs7E+LFSviCEOuPVpKsLOkgHluduUB2h3WPKmQr+Y/5YcKTtzAJHIc4/FZML4ky5CyqxQrepWBY0uw3vcCXOODgfl49ZNc2Cr5bnf9o8LWZwuhGzLgAGQnvHXfdnBpj6HTzHkYfgsbuFOakeqYLuAfMecLmbQwIxsWalLKuoL5ajz085V4zAWYq+bSW3QClZSOZUj6l35EdZdUS1cWDhSvdFifPLQBDDqByHtOY7UPcOihlyam0kobA2vfyheD7UCMeHyGwDXeAg02xGrqrUR7iZHbvEJiGTMp1hTudNGxV7elyq79Z2z4NmCrp0WgXIXpTQLEGwR5b3BYbfMX6aQP0G0xOKy5GbHpNDVbeq0dvvU6fhcenGvmRf8A5M0p3LG/UHJjRGKdTlKMYoiYGaKKKI2cguSkVklEsYFxLUJHs3jt9JheOweAzAx8SdXt/wCphyT234uod92YwUFxWq63/lLuTBizDcANX1Ltv6+cw+x+IDLR6iaOMsrCgCK3B2Nek5L2tF9101rWa5MKvH9mPiondTyYfsfIymZ1HAcfdo6jQQdQJ63yI6jqDI8X+GNXiwEbgnSx2FdFY8z6GdPH+R8s5uX8f7VzQE1sV4kFspBcEad9yrcz5b37gQa9gcSSR3L7eYofc7QPaDlFKafzEV1UfxFdLMf+4AS+W8TGRJcNJidmA+L7SPCnvs9uAi7qN9QA1ED3Umc0fx9wjZSxw5VLH/eeHb/mQNuJt9s8KTjbEx1bNkDHyd3ZV+AK+Z43xGEoxVhRUkH4mFrzEOmlImZ17rwfbJcp3ZDY3B8YN0R0/l6VJ5+0hiRFyvZJ0Bm5sd6nl34G7WZGbDex8ajyYfVXuN/+mdhxHFrkC6hdEEehHlDqY1Fpms40eP40oV0KW1MAa6A9Z2fBcTwZFHEbVBvZOonY2DsfP5nm/wDtIDIqlqLHYHr1M6Ph85xlmR9Luo5AE0LqjVj4imulFlriMODCX04ExIA7MB1QiyWHIDnJpbNjdMgUFbCciykCtuYraZfBcQCzLkOrI41ENuzdBd8x/SbPZuFslZHxFWWwt1YUmtVg8jXL2kz20rIODh6B7ggB8t5SSW/5632Ow26by/j4Zw+R1yHUyrpVx+UpGqiD19ZE4SFdUpFO6sBspN2x6Hc3DDDYGJ8rasehjSgk9Rvys10/SQ0hawDLaMxSgnjoG9fmpPJeexEq/wCJbTldMeTWxJ05DzZVpdO50qaHL384M5cQyd4C5y5MXhxOzBaU/wAG4U70SBLXDlaZwD3mQLaO1hdIAIWh7m+pjyRsMviMNYwKZdTC6yMCpO5Ic70PL/1C8XjOpDSkLbMWGrw0b0uPpbl5WJZ4TtBsbFO7CElqX6g4HI3/AJSRC94AwdkKGgAATuG3ogc6rmfiPAwu0eBDawpAJUNWmm0EeE6jsxG+/LzAmfxSaVRCNd0HNAVammZegJWp0js9pqYlNlsAHUSCDqU/TTVR+/WQ4jEuvSy76dIevKiAaO3nv/7qGcxrzxx+Yyqd1bSfQkA19jOkAFCrqhV86oc5W4/gEpsunTb+LodQrHf2A38pezL9J8x+xI/lNePqzm5Y6AMjJsIJzN2CRMjcGWkS8DF1RQGqKLTAUSYMCsJNE6nkaxUxTwNNc2hHKCZXp5NeO/j7B4HGVnQ4+KBxlyCSo3rntvdTHWWOEz6W9D/dzPk4tpn1dOXL78aPdKxHiNEEU24Ibfn0mn2Z2hk1KAAcen6r3DA1XqCP2mcQ1WNPzvf2j4eIGNdTabHMrfpvp+ROWM9OuXQ9tdqZSNKMNRXw6rqx7TkGzEZsj5SBWXRiNclyDGAD6lxNTOdRR2JFUdN8iCeo53dTne0MT9+zMfA9EDqGV1yKfe7H/TNKRibWXe0srquQ5BYGRVUqL8DFQrV6E7+04r8Vfh7WA6isgFEdWA5e9crnoKLrOM66Gpiy7HWNJGmvQkH4mZ2nwuPLlUg/SpKkHmG5/sDLz+spmY7h5t+EezW/xAZgVCatVgjfSdvsR95tEMrObsHTQ8iNj95p8biyKwXYknbbmV5E9OUq8PweQs2ogg1Xp5g/MXj1EQm19nZPwXBLkdHa7Q2vzt8zsOF7NDZVyi9aoVAvajvymF2ZweRHYmq20Vz5b38zp+yMBGNTkeslUSvqQTQ9qHzHIqscLwWMt3lqHUhSxB2AIZlHxZ+ZfPEUpdF1FnBIYgeEkDlW1DepV70sVoCg3iYgbDyX1NgfeXD2LyKsaVtY3rc3akf5hbE+8iW8dKXFOzAKqKcIOjIv06VZdtIqm8tpo4cGTI1rSirsncG+VVCcRjxAVk8VjxX9q2mfxH4lw4VNlVHLc7+kmcGy1VwIWJKHYGzys8gB6c785LhWxY/r+sb2aBvkannHbP8A8jf5cCmvcge2/T+s5XN+KOLfZstcvpABHpfOodp8oe55eKxG99+XID9feEPaOEAA70KuhuPWeCHtXiD/AMbJ943+2OJH/Gb5o/uJM6POHu+btTAo61sTR5EevWYn+Ox5MxOvw+d7Ued+c8cbtfPk8JyMR9h+k7P8PYGZByI62ahEyrYdhxQVgUIBLHSB51qP7KT8TExBwND76L8R62Sar0mrwx0KKOog0fgjffrUHxNFif4qY1y1UAa9PDN6z2x5I6UWWBfHcuESJWa+TnxQbFId1L7JBlYeR4rDDFLYWKLTxiLCiCWFm7PCEKIK44aLRgpkLiuRiGNXgMh0V9pAo+q68Nk7chXTeT4cUoj8aHIARgCBuTVfacHN1byh38X/ADg2QjPjZSaUiudMD5g9OhmF2zkcasS7s2PI6Of/ALBVL7Gz9ouza7zutTMu9nevFd8unvND8R8KQi5U3KbH1Uiifjn95pWdjSv0H2EW1vrUbEPiN70yaWB+QfuJYzdl6FPdbFQxW+VsS1e13MrsDjT3q4ytCwNX8V+f6G/IidJ26DoKY205CLB6UCLv9vmWhn8KjPjVsi6XIBI/hNbiUeG4hDxX+HI8WjXdbVdc5Y7Y4lkAJNAmtt+Y2huwMDNjV3C95yLDqL6enKTG6U4sUe9XHpNaGbV02IGn33v4h8GSmoclc6tQPJlvwn3ofB8ofic+jTQJs0SK2FHc+m1fMzcuZmK6iFJLWoOxG45/KmUPTa4LigwZQFOkeI3yuyK68qmX+KO3cnC49f1cgBfnt8QXDk4k8KkggUxPQLQJr2/WUO1UPEYiC2q+n7bTO0K8nNcb+Ms+X6aUfczF4jKzHU7Fj6/yl4/hzMGIQH0vl7XI5Pw1xR+rC1D+Eg/sbk5iJnWQ+ReXP2jHNvsD9q/edHwXYLHY4WFf6CP5Ta4b8JZDRGE/O37w2T8YcRjGQ7gRW58Ok2dv7qemcN+B2YUxA9F8R/oJv9jfgXFiYOF3828R/oPiHavGHnvY34Wy+FmXqCROq4jhBix3yvah1Jna8QcWIVQv+/1nJdsZdZ5bDpDYw8MXCIo1DnZryNA++0i4omjsSSPnf97mWM5d658gD7bkj++k0CDQvc0N5dGfJ6SDydwCoZZRZpLKESsGyy3UBnhp4qtkigX5xQNmpDQCmGqbsyaRuIiKoEQMmh3EjUmg3ENNqNRoQPGEopJIv9gfOGwqQwqUu2MTOa8VUdqnn8kzuO7jiMY2DVbDUUFgjIOZ3uq/T5nSpx2oHqoG48x1nJPgZcYQNZDaW5g15g9YfFxZY6MbkHE41bbMKsrv7iaVsVoW+18D4mZlAOkKyC6sEHa+nTf1m32fxDZE1PWqzsLFDmoO93RqY2XizdFdQ+9DqB6R047unbVq011U2PLbz/lHE9omGp2ymMppyBipu6Ok+WxljDxiJjxjGavYegA/8TH4vK+VK1DaipBsHqDW32mZpdlCs+l1vddwFN3V+lfrK1M9Onxtk0ku2prYqa6EkqK9BtIKbRHfGQw6AaipPMWOm0p8ZxwrEEI/13zIogV8wGTj21KgYkgWbBoi+jecNJraw2MjUKA5j023HxW0ocG6iir2ObHnZ617V+kpHi92A2XfUpvruSPmERUABCkA8wN69fUGRNjiHY9m8TiKjl7zYxZcIq6+JwuC1rTvfIb8vOaClyNxVev9ZHm0irthn4byEKmbhh5facOqG/q+JLUQK1TObKirtsvauFfpmPxfbzbhZhr68vXlCYwvTevsJOqw7uz7sZn5cJY7bC9/76S7mZuSge7b/wD5H9ZT4LKzFw3NWrlQl1raUTesekOH4QKSwHPl8SwVkmkdc6I6c8zs6kqycGDCKY9GI6jIOtw+mOFi0Yzzwx6RTR7uKHkeOTWEEAph1nRrM7RASapCrji0YEFk0WHCxwsWjBMmTbeGbxJW978jvA5cdgHykuDyeI6j4iNh51OXkdVJYXaPCgN1BQ3fRiQdplLmfUoKValiQeTWPDN/tLDY3FqLYeZYXsJzxosVBIYU5UncBuX7SayqzT/DGM6qyNr0sxJ8uZW/0l3j8JLHTuC29n9IHsHZCQml8jHXfOl8IP6TW7tQKr3l1iZZ2vEOVycU2JjpBK/wnpXl6QT8YuU6lfuzRB1cwfbkRNnjuDIOpN75i5UPZmN2oqQ291XvuP5ybRMHWYsrcJ2yuIgZAAeWrcrfL4+Zu8Dhx5FsDmSbVrFnyFnrM1ewMO4FvY3BqgPK5q8F+EuHHiUOnquRl/YyJsuKijshSu9t+hH6yxwfZXdrXT1uWsXZaKR+blNdGYN+pW/1lpuHShqXUD5kkfIkeSvFURl/jW/cAfaJ3XkMbv8A8qkD21NQmhj4ZE+hFW/4VA/aFv7xaeMlsOZhtjCD/XkJPyEv94fH2YdtWSq6IqqP1sy/q6SSL6+0RhpgQWQBfmdz9zGzPtUI7VKeUzXjpvcsuW+dQcGRNCyBz5wPeSL5p0Y59O0Ewke+hsZgEUUyyiSeJJYVItVgKrJaYfRFoi08A0xpZ0RRaeOFQSzjWV0lrEZ0azwfGsIBBXJrFpYnG1SJeRBiGDhr2lBwUcMLJU37CWhC6FcUdj5yLxrSk4FmPeYwVFXdjqDOex8FqJDg6yFDMvOrNG/idAiZkNqFIPMHkRLCOf4Qt+X9ZlWk60teMVcGEIK5nz61GYmXUwSRwTeJxzTsqWPGSd9hMPtU/mCufMEbGvedVkxAIb6ic5xyKAXHQbeZAk2nWlYwTsztlPpyrvYBZRdrW5brc6fAVZRpNr0nnZbURp6iyPedn2OdOJRyqZTx76XHJkdtvGN9/iTEqYuIMtI0znjtDSOSspBep+0IF6yOraRZ4o47Sc8lYGkGyQeqQeaV44j2ztyzPo2TNK5Nx2UmSRJqxVnSBbGZp92IhiENHizsfCy7iwVDqkKBFMqiEEWFURKI9yZUKBGYRlaJmiM0UjqiiDhkEsYzKZyySZpvqZhoBo4aUe+k0ybxpWxCosjiIlrCITIwNsUfFjlvREuOTow2mRKw1RBLhoQWRcyyMMi2CGlirxKgoL6zA4zhrbXq8IXTXSdFxaeGYHEcPqtQaULv52ZlM9to9MxyFblsFux+03+EfwiuUxC6p4T/AJAPtOh4BLUbS4lnaFvhTc1cK7Srw+CXl2jmUxCDwZWFYxotPAwsRWOcgg3yQNKpDTIq0KqxA6LC6Y+NYcLFpq4SS0wxgMmSI01WOwgEzQjZIGYGMxgmeR1QMmyRSBMeAxwVkw+MQeHeFCG5powTRDYkjY1lvBij1ODYcYl7CsHjQSykWjEgsmFklkwItGBERLDaZBlhoxNXj6rlVzUjjeBD5ksTnuNwjcHbeyR6To1NzM7XwbGpFl1c5anc86+4J2nT9mUUFTldZORR5mh6zsOGxaVA9I4Ky9jMkzSupMKsaC3jPDhJBxFpqbSSJcLokwIaWGx4oZUgdUMjRGlUcNEZAwNNnlTMbhMjQAEDMiwveCMyynmJga1rEE+SpWUmNkMAsjJFApyiiNyXAy6YopZi4ucuYI8UZLeOFEUURD44aKKIFINFFAlfLIJFFGmVrHK/aP0GPFJt6VX25fD/AL1PcTrxHihU7HhMcUUaBxBmKKIISRiigcImTxxRRGmJGKKAReREUUDOZWyRRQMEyEeKBGeKKKJb/9k=",
    		"https://t4.ftcdn.net/jpg/02/53/61/69/360_F_253616948_za22DUrpvoM6aBDyPZxXDXf1OVNZFhL4.jpg",
        "https://www.aqueon.com/-/media/project/oneweb/aqueon/us/blog/top-5-aggressive-fish-tank-species-for-your-home-aquarium/shutterstock-2175454025-web.jpg",
        "https://images.theconversation.com/files/299379/original/file-20191030-154716-1wc4d64.jpg?ixlib=rb-4.1.0&rect=0%2C12%2C2048%2C1023&q=45&auto=format&w=1356&h=668&fit=crop",
        "https://www.marlinmag.com/wp-content/uploads/2023/10/black-marlin-hooked-panama-tropic-star-lodge-1024x768.jpg",
        "https://cdn.prod.www.spiegel.de/images/b237b5e1-2d68-41d9-b233-4e7ff03514d5_w960_r1.778_fpx48_fpy49.jpg"
           );
    
    private Random random = new Random();
    
    public RegisterController() {
        this.passwortBestaetigung = "";
        this.fehlermeldung = "";
    }
    
    public String registrieren() {
        fehlermeldung = "";
        
        if (!validiereEingaben()) {
            return null; // Auf der gleichen Seite bleiben
        }
        
        try {
         
            
            personenListe.personHinzufuegen();
            
            Person neuerNutzer = personenListe.getPersonen().get(personenListe.getPersonen().size() - 1);
            personenListe.setAktuelleIndex(personenListe.getPersonen().indexOf(neuerNutzer));
            
            return "dashboard.xhtml?faces-redirect=true";
            
        } catch (Exception e) {
            fehlermeldung = "Ein unerwarteter Fehler ist aufgetreten. Bitte versuchen Sie es erneut.";
            return null;
        }
    }
    
   
    private String getRandomImage() {
        return DEFAULT_IMAGES.get(random.nextInt(DEFAULT_IMAGES.size()));
    }
    
 
    
    private boolean validiereEingaben() {
        Person neuePerson = personenListe.getNeuePerson();
        boolean isValid = true;
        StringBuilder fehler = new StringBuilder();
        
        if (neuePerson.getName() == null || neuePerson.getName().trim().isEmpty()) {
            fehler.append("Name ist ein Pflichtfeld. ");
            isValid = false;
        } else if (neuePerson.getName().trim().length() < 2) {
            fehler.append("Name muss mindestens 2 Zeichen lang sein. ");
            isValid = false;
        }
        
        if (neuePerson.getRollenTyp() == null) {
            fehler.append("Bitte wählen Sie eine Rolle aus. ");
            isValid = false;
        }
        
        if (neuePerson.getPasswort() == null || neuePerson.getPasswort().trim().isEmpty()) {
            fehler.append("Passwort ist ein Pflichtfeld. ");
            isValid = false;
        } else if (neuePerson.getPasswort().length() < 6) {
            fehler.append("Passwort muss mindestens 6 Zeichen lang sein. ");
            isValid = false;
        }
        
        if (passwortBestaetigung == null || passwortBestaetigung.trim().isEmpty()) {
            fehler.append("Passwort-Bestätigung ist ein Pflichtfeld. ");
            isValid = false;
        } else if (!neuePerson.getPasswort().equals(passwortBestaetigung)) {
            fehler.append("Die Passwörter stimmen nicht überein. ");
            isValid = false;
        }
        
        if (neuePerson.getTelefonnummer() != null && !neuePerson.getTelefonnummer().trim().isEmpty()) {
            String telefon = neuePerson.getTelefonnummer().trim();
            // Einfache Validierung: nur Zahlen, Leerzeichen, +, -, (, ) erlaubt
            if (!telefon.matches("[0-9+\\-\\s()]+")) {
                fehler.append("Telefonnummer enthält ungültige Zeichen. ");
                isValid = false;
            } else if (telefon.replaceAll("[^0-9]", "").length() < 6) {
                fehler.append("Telefonnummer muss mindestens 6 Ziffern enthalten. ");
                isValid = false;
            }
        }
        
        
        if (neuePerson.getBild() == null || neuePerson.getBild().trim().isEmpty()) {
            String randomImage = getRandomImage();
            neuePerson.setBild(randomImage);
            System.out.println("Zufälliges Bild zugewiesen: " + randomImage);
        }

    
       
        
        if (isValid && neuePerson.getName() != null && !neuePerson.getName().trim().isEmpty()) {
            if (nameExistiertBereits(neuePerson.getName().trim())) {
                fehler.append("Ein Nutzer mit diesem Namen existiert bereits. ");
                isValid = false;
            }
        }
        
        if (!isValid) {
            fehlermeldung = fehler.toString().trim();
        }
        
        return isValid;
    }
    
    private boolean nameExistiertBereits(String name) {
        return personenListe.getPersonen().stream()
            .anyMatch(person -> person.getName().equals(name));
    }
    
    public String getPasswortBestaetigung() {
        return passwortBestaetigung;
    }
    
    public void setPasswortBestaetigung(String passwortBestaetigung) {
        this.passwortBestaetigung = passwortBestaetigung;
    }
    
    public String getFehlermeldung() {
        return fehlermeldung;
    }
    
    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }
    
    public List<String> getDefaultImages() {
        return DEFAULT_IMAGES;
    }
}