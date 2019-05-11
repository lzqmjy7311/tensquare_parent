package cn.tensquare.spit.service;

import cn.tensquare.spit.dao.SpitRepository;
import cn.tensquare.spit.po.Spit;
import com.itcast.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SpitRepository spitRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 发布吐槽（或吐槽评论）
     *
     * @param spit
     */
    public void saveSpit(Spit spit) {
        //主键
        spit.setId(idWorker.nextId() + "");
        //默认值
        spit.setPublishtime(new Date());//发布时间为当前时间
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //保存新的吐槽
        spitRepository.save(spit);
        //判断是否存在上级id--处理对某个吐槽进行评论吐槽后，父吐槽的评论数+1
        if(!StringUtils.isEmpty(spit.getParentid())){
            //如果存在，则说明是在回复吐槽，则需要将被回复的吐槽的回复数+1
            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("_id").is(spit.getParentid()))
                    ,new Update().inc("comment",1)
                    ,"spit");
        }
    }

    /**
     * 修改
     *
     * @param spit
     */
    public void updateSpit(Spit spit) {
        spitRepository.save(spit);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteSpitById(String id) {
        spitRepository.deleteById(id);
    }

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Spit> findSpitList() {
        return spitRepository.findAll();
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Spit findSpitById(String id) {
        return spitRepository.findById(id).get();
    }

    /**
     * 根据上级ID查询吐槽列表
     *
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findSpitListPageByParentid(String parentid, int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page - 1, size);

        return spitRepository.findByParentid(parentid, pageRequest);
    }

    /**
     * 使用底层的MongoTemplate工具类来直接操作
     * 根据吐槽的id，增加点赞的数量
     *
     * @param id
     */
    public void updateSpitThumbupToIncrementing(String id) {
        //构建查询条件对象
        Query query = new Query();
        //添加条件
        query.addCriteria(Criteria.where("_id").is(id));

        //构建要修改的对象
        Update update = new Update();
        //对点赞数递增1
        //参数1：递增的字段，参数2：递增的步进，这里是每次+1
        update.inc("thumbup", 1);
        //update.set(key,value)//更新部分字段

        //更新符合条件的第一条记录，
        //参数1：查询条件对象；参数2：要修改的对象；参数3：映射的实体类或集合名字
        //db.spit.update({_id:"213123123123"},{$inc:{thumbup:NumberInt(1)}})
        mongoTemplate.updateFirst(query, update, "spit");
        //更新多条文档-参考
        //mongoTemplate.updateMulti(Query.query(Criteria.where("content").is("吐槽测试01")),Update.update("visits",1100),Spit.class);

    }
}