package edgecloud.lambda.controller;

import com.sun.org.apache.xpath.internal.functions.FuncId;
import edgecloud.lambda.entity.Function;
import edgecloud.lambda.entity.FunctionNodeMap;
import edgecloud.lambda.entity.Node;
import edgecloud.lambda.repository.FunctionNodeMapRepository;
import edgecloud.lambda.repository.FunctionRepository;
import edgecloud.lambda.repository.NodeRepository;
import edgecloud.lambda.utils.FunctionIO;
import edgecloud.lambda.utils.GetConfig;
import edgecloud.lambda.utils.StubServer;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class FunctionController {

    private static final Logger log = LoggerFactory.getLogger(FunctionController.class);

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private FunctionNodeMapRepository fnmRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping("/functions")
    public String listFunctions(Model map) {
        log.info("Querying function...");
        List<Function> functions = functionRepository.findAll();

        for (Function function : functions) {
            log.info(function.toString());
        }

        map.addAttribute("functions", functions);
        return "pages/functions";
    }

    @PostMapping("/create")
    public String createFunction(@ModelAttribute Function function) throws IOException {
        log.info("Creating lambda function: " + function.toString());

        log.info("Generating storage path for function...");
        String funcDir = new GetConfig().readConfig("application.properties")
                .getProperty("edgecloud.lambda.functionLocation");
        String funcPath = String.format("%s/%s/%s", funcDir, function.getFuncName(), function.getFuncVersion());
        log.info("Function storage path is: " + funcPath);
        function.setFuncPath(funcPath);

        // check whether function with same name exist
        Function current = functionRepository.findByFuncNameAndFuncVersion(function.getFuncName(), function.getFuncVersion());
        if (current == null) {
            // create new function
            Function res = functionRepository.save(function);
            log.info("Lambda function created: " + res.toString());
            FunctionIO.writeFunction(res.getFuncBody(), res.getFuncPath());
        } else {
            // update funcBody of current function
            current.setFuncBody(function.getFuncBody());
            functionRepository.save(current);
            log.info("Lambda function with same funcName and funcVersion existed, updated: " + current.toString());
            FunctionIO.writeFunction(current.getFuncBody(), current.getFuncPath());
        }

        return "redirect:/functions";
    }

    @PostMapping("/push")
    public String pushFunction(@ModelAttribute FunctionNodeMap fnm) {

        log.info("Pushing lambda function to node: " + fnm.toString());
        log.info("Function Id: " + fnm.getFuncId());
        log.info("Node Id: " + fnm.getNodeId());

        Function function = functionRepository.findOne(fnm.getFuncId());
        List<Node> nodes = new ArrayList<>();
        List<Node> allNodes = StubServer.queryNodes();

        if (fnm.getNodeId().equals(-1)) {
            log.info("Pushing lambda function to All Nodes...");
            nodes = allNodes;
        } else {
            log.info("Pushing lambda function to one Node...");
            for (Node node : allNodes) {
                if (node.getId().equals(fnm.getNodeId())) {
                    nodes.add(node);
                    break;
                }
            }
        }

        log.info("Destination nodes size is: " + nodes.size());
        for (Node node : nodes) {
            log.info("Pushing function to node: " + node.toString());

            FunctionNodeMap _fnm = new FunctionNodeMap();
            _fnm.setFuncId(fnm.getFuncId());
            _fnm.setNodeId(node.getId());

            try {
                StubServer.pushFunctionToNode(_fnm.getNodeId(), function);
            } catch (Exception e) {
                log.error("Push lambda function failed, destination is: " + _fnm.getNodeId());
                continue;
            }

            // check whether same fnm exists
            if (fnmRepository.findByFuncIdAndNodeId(fnm.getFuncId(), fnm.getNodeId()) != null) {
                log.info("Same fnm exists, no need to save function-node map again, skipping.");
                continue;
            }

            fnmRepository.save(_fnm);
            log.info("Push lambda function succeed, destination is: " + _fnm.getNodeId());
        }

        return "redirect:/";
    }

}
