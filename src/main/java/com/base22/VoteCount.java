package com.base22;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Hello world!
 */
public class VoteCount extends Configured implements Tool {

	public static void main( String[] args ) throws Exception {
		int res = ToolRunner.run( new Configuration(), new VoteCount(), args );
		System.exit( res );
	}

	public int run( String[] args ) throws Exception {
		if ( args.length != 2 ) {
			System.out.println( "Incorrect input, expected: [input] [output]" );
			System.exit( - 1 );
		}

		Configuration conf = this.getConf();

		// Create new Job

		Job job = new Job(conf, "Tool Count Job");
		job.setJarByClass( VoteCount.class );

		// Set job-specific parameters

		job.setOutputKeyClass( Text.class );
		job.setOutputValueClass( Text.class );

		job.setMapperClass( VoteCountMapper.class );
		job.setReducerClass( VoteCountReducer.class );

		job.setInputFormatClass( TextInputFormat.class );
		job.setOutputFormatClass( TextOutputFormat.class );

		job.setMapOutputKeyClass( Text.class );
		job.setMapOutputValueClass( IntWritable.class );

		FileInputFormat.setInputPaths( job, new Path( args[0] ) );
		FileOutputFormat.setOutputPath( job, new Path( args[1] ) );

		// Submit job and poll its status.

		job.submit();
		return job.waitForCompletion( true ) ? 0 : 1;
	}
}